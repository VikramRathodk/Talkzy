package com.devvikram.talkzy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.ChatMessage
import com.devvikram.talkzy.data.firebase.models.Conversation
import com.devvikram.talkzy.data.firebase.models.FirebaseContact
import com.devvikram.talkzy.data.firebase.models.MessageStatus
import com.devvikram.talkzy.data.firebase.repository.FirebaseContactRepository
import com.devvikram.talkzy.data.firebase.repository.FirebaseMessageRepository
import com.devvikram.talkzy.data.firebase.repository.FirebaseMessageStatusRepository
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.devvikram.talkzy.data.room.models.RoomMessageStatus
import com.devvikram.talkzy.data.room.models.RoomParticipant
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
import com.devvikram.talkzy.data.room.repository.MessageStatusRepository
import com.devvikram.talkzy.data.room.repository.ParticipantRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val conversationRepository: ConversationRepository,
    private val contactRepository: ContactRepository,
    private val firebaeContactRepository: FirebaseContactRepository,
    val loginPreference: LoginPreference,
    private val messageRepository: MessageRepository,
    private val firestore: FirebaseFirestore,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    private val participantRepository: ParticipantRepository,
    private val messageStatusRepository: MessageStatusRepository,
    private val firebaseMessageStatusRepository: FirebaseMessageStatusRepository

) : ViewModel() {


    companion object {
        private const val TAG = "AppViewModel"

    }

    private val _currentConversationId = MutableLiveData<String>("")
    val currentConversationId: LiveData<String> get() = _currentConversationId

    private var conversationListenerRegistration: ListenerRegistration? = null
    private var contactListenerRegistration: ListenerRegistration? = null

    val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    val _isOnBoardingCompleted = MutableLiveData(false)
    val isOnBoardingCompleted: LiveData<Boolean> = _isOnBoardingCompleted

    private val messageListeners = mutableMapOf<String, ListenerRegistration>()

    init {
        _isLoggedIn.value = loginPreference.isLoggedIn()
        _isOnBoardingCompleted.value = loginPreference.isOnBoardingCompleted()

    }


    fun listenToContacts() {
        println("Listening to contacts")
        firebaseFirestore.collection(FirebaseConstant.FIRESTORE_CONTACTS_COLLECTION)
            .addSnapshotListener { snapShots, e ->
                if (e != null) {
                    Log.d(TAG, "listenToContacts:  ${e.message}}")
                    return@addSnapshotListener
                }
                snapShots?.documentChanges?.forEach {
                    val contact = it.document.toObject(FirebaseContact::class.java)

                    Log.d(TAG, "listenToContacts: $contact")

                    viewModelScope.launch {
                        when (it.type) {
                            DocumentChange.Type.ADDED -> {
                                println("contact: Document added: ${it.document.data}")
                                contactRepository.insertContact(
                                    ModelMapper.toRoomContact(
                                        contact
                                    )
                                )
                            }

                            com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                println("contact: Document modified: ${it.document.data}")
                                contactRepository.insertContact(
                                    ModelMapper.toRoomContact(
                                        contact
                                    )
                                )
                            }

                            com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                println("contact: Document removed: ${it.document.data}")
                                contactRepository.deleteContactById(contact.userId)
                            }

                            else -> {
                                println("Unknown contact document change type: ${it.type}")
                                // Handle other event types
                            }
                        }
                    }
                }
            }
    }

    fun listenToConversation() {
        conversationListenerRegistration?.remove()
        println("Listening to conversation")
        conversationListenerRegistration =
            firebaseFirestore.collection(FirebaseConstant.FIRESTORE_CONVERSATION_COLLECTION)
                .whereArrayContains("participantIds", loginPreference.getUserId())
                .addSnapshotListener { snapShots, e ->
                    if (e != null) {
                        println("Listen failed: ${e.message}")
                        return@addSnapshotListener
                    }

                    snapShots?.documentChanges?.forEach {
                        println("Document change: ${it.document.data}")

                        val conversation = it.document.toObject(Conversation::class.java)
                        Log.d(TAG, "listenToConversation: $conversation")

                        viewModelScope.launch {
                            when (it.type) {
                                DocumentChange.Type.ADDED -> {
                                    println("Document added: ${it.document.data}")
                                    conversationRepository.insertConversation(
                                        ModelMapper.toRoomConversation(
                                            conversation,
                                            loginPreference.getUserId()
                                        )
                                    )
                                    // 2. Save participants
                                    val roomParticipants = conversation.participants.map { p ->
                                        RoomParticipant(
                                            conversationId = conversation.conversationId,
                                            userId = p.userId,
                                            localParticipantId = 0,
                                            role = p.role,
                                        )
                                    }
                                    participantRepository.insertAllParticipants(roomParticipants)
                                    if (conversation.type == "P") {
                                        val loggedInUserId = loginPreference.getUserId()
                                        val receiverId =
                                            conversation.participantIds.firstOrNull { it != loggedInUserId }
                                                ?: return@launch
                                        val existingContact =
                                            contactRepository.getContactById(receiverId)
                                        if (existingContact != null) {
                                            contactRepository.insertContact(
                                                existingContact.copy(
                                                    conversationId = conversation.conversationId
                                                )
                                            )
                                            firebaeContactRepository.updateField(
                                                existingContact.userId, mapOf(
                                                    "conversationId" to conversation.conversationId,
                                                    "lastModifiedAt" to System.currentTimeMillis()
                                                )
                                            )

                                        }
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    println("Document modified: ${it.document.data}")
                                    // 2. Save participants
                                    val roomParticipants = conversation.participants.map { p ->
                                        RoomParticipant(
                                            conversationId = conversation.conversationId,
                                            userId = p.userId,
                                            localParticipantId = 0,
                                            role = p.role,
                                        )
                                    }
                                    participantRepository.insertAllParticipants(roomParticipants)
                                    conversationRepository.insertConversation(
                                        ModelMapper.toRoomConversation(
                                            conversation,
                                            loginPreference.getUserId()
                                        )
                                    )
                                }

                                DocumentChange.Type.REMOVED -> {
                                    println("Document removed: ${it.document.data}")
                                    conversationRepository.deleteConversation(conversation.conversationId)
                                    participantRepository.deleteParticipantsByConversationId(
                                        conversation.conversationId
                                    )
                                }

                                else -> {
                                    println("Unknown document change type: ${it.type}")
                                }
                            }
                            syncMessages(conversation.conversationId)
                        }


                    }

                }
    }


    fun syncMessages(conversationId: String) {
        val TAG = "SyncMessagesViewModel"
        Log.d(TAG, "syncMessages: 🔄 Starting sync for conversation $conversationId")

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = formatter.format(Date())
        val currentUserId = loginPreference.getUserId()

        Log.d(TAG, "syncMessages: Current user ID: $currentUserId, Date: $currentDate")


        Log.d(TAG, "syncMessages: Setting up new listener for conversation $conversationId")
        messageListeners[conversationId] =
            firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
                .document(conversationId)
                .collection(currentDate)
                .orderBy("lastModifiedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.e(TAG, "❌ Error listening to messages: ${error.message}", error)
                        return@addSnapshotListener
                    }

                    Log.d(
                        TAG,
                        "📥 Message snapshot received: ${snapshots?.documentChanges?.size ?: 0} changes, fromCache: ${snapshots?.metadata?.isFromCache}, hasPendingWrites: ${snapshots?.metadata?.hasPendingWrites()}"
                    )

                    if (snapshots != null && !snapshots.metadata.isFromCache) {
                        snapshots.documentChanges.forEach { change ->
                            val firebaseMessage = change.document.toObject(ChatMessage::class.java)
                            Log.d(
                                TAG,
                                "📌 Message event: ${change.type} -> messageId: ${firebaseMessage.messageId}, senderId: ${firebaseMessage.senderId}"
                            )


                            viewModelScope.launch {
                                if(change.type == DocumentChange.Type.REMOVED){
                                    deleteMessageFromLocalDatabase(firebaseMessage.messageId)
                                    delteMessageStatusFromLocalDatabase(firebaseMessage.messageId)
                                }
                                val existingMessage = messageRepository.getMessageById(firebaseMessage.messageId)
                                Log.d(TAG, "Message ${firebaseMessage.messageId} exists in local DB: ${existingMessage != null}")

                                if (existingMessage == null) {
                                    val roomMessage = ModelMapper.mapToRoomMessage(firebaseMessage)
                                    messageRepository.insertMessage(roomMessage)

                                    if (firebaseMessage.senderId != loginPreference.getUserId()) {
                                        handleNewMessage(roomMessage, conversationId)
                                    } else {
                                        // Add status for sender message
                                        addSenderMessageStatus(roomMessage)
                                    }
                                }else{
                                    Log.d(TAG, "syncMessages: Message ${firebaseMessage.messageId} already exists in local DB")
                                }

                            }

                            Log.d(TAG, "Setting up status listener for message ${firebaseMessage.messageId}")
                            listenToStatusUpdatesForConversation(conversationId)
                        }
                    } else {
                        Log.d(TAG, "Ignoring snapshot: fromCache=${snapshots?.metadata?.isFromCache}, hasPendingWrites=${snapshots?.metadata?.hasPendingWrites()}")
                    }
                }
    }

    private fun addSenderMessageStatus(message: RoomMessage) {
        val currentUserId = loginPreference.getUserId()
        val currentTime = System.currentTimeMillis()
        val status = RoomMessageStatus(
            messageId = message.messageId,
            userId = currentUserId,
            receivedAt = currentTime,
            readAt = currentTime
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                messageStatusRepository.addOrUpdateStatus(status)
                firebaseMessageStatusRepository.insertReadStatus(
                    ModelMapper.mapToMessageStatus(status)
                )
                Log.d("AppViewModel", "Inserted sender message status for ${message.messageId}")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error inserting sender status", e)
            }
        }
    }

    private fun delteMessageStatusFromLocalDatabase(messageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageStatusRepository.deleteStatusByMessageId(messageId)
        }
    }

    private fun handleNewMessage(message: RoomMessage, conversationId: String) {
        val currentUserId = loginPreference.getUserId()
        val currentTime = System.currentTimeMillis()
        val TAG = "MessageStatusViewModel"

        val isOpen = isConversationOpen(conversationId)
        Log.d(TAG, "handleNewMessage: Conversation $conversationId is open: $isOpen")

        viewModelScope.launch(Dispatchers.IO) {
            val messageStatus = RoomMessageStatus(
                messageId = message.messageId,
                userId = currentUserId,
                receivedAt = currentTime,
                readAt = if (isOpen) currentTime else null
            )

            Log.d(TAG, "handleNewMessage: Creating status - messageId: ${messageStatus.messageId}, userId: ${messageStatus.userId}, readAt: ${messageStatus.readAt}")

            try {
                messageStatusRepository.addOrUpdateStatus(messageStatus)
                Log.d(TAG, "handleNewMessage: Local status saved successfully")
            } catch (e: Exception) {
                Log.e(TAG, "handleNewMessage: Error saving local status", e)
            }

            try {
                Log.d(TAG, "handleNewMessage: Sending status to Firebase for message ${message.messageId}")
                val firebaseStatus = ModelMapper.mapToMessageStatus(messageStatus)
                firebaseMessageStatusRepository.insertReadStatus(firebaseStatus)
            } catch (e: Exception) {
                Log.e(TAG, "handleNewMessage: Error sending status to Firebase", e)
            }
        }
    }
    // In AppViewModel.kt
    private val statusListeners = mutableMapOf<String, ListenerRegistration>()

    fun listenToStatusUpdatesForConversation(conversationId: String) {
        val TAG = "StatusListenerViewModel"

        statusListeners[conversationId]?.remove()

        Log.d(TAG, "Setting up status listener for conversation $conversationId")

        viewModelScope.launch {
            val messageIds = messageRepository.getMessageIdsForConversation(conversationId)

            if (messageIds.isEmpty()) {
                Log.d(TAG, "No messages found for conversation $conversationId")
                return@launch
            }

            Log.d(TAG, "Listening for status updates on ${messageIds.size} messages")

            messageIds.forEach { messageId ->
                val messageStatusListener = firestore
                    .collection(FirebaseConstant.FIRESTORE_MESSAGE_STATUS_COLLECTION)
                    .document(messageId)
                    .collection("message_status")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e(TAG, "Error listening to status updates for message $messageId", error)
                            return@addSnapshotListener
                        }

                        Log.d(TAG, "Status updates received for message $messageId: ${snapshot?.documents?.size ?: 0}")

                        snapshot?.documentChanges?.forEach { change ->
                            val status = change.document.toObject(MessageStatus::class.java)
                            Log.d(TAG, "Processing status update: ${status.messageId}, ${status.userId}")

                            viewModelScope.launch(Dispatchers.IO) {
                                messageStatusRepository.addOrUpdateStatus(
                                    ModelMapper.mapToRoomMessageStatus(status)
                                )
                            }
                        }
                    }

            }
        }
    }


    // In AppViewModel.kt
    fun setCurrentConversationId(conversationId: String) {
        val previousId = _currentConversationId.value
        Log.d("ConversationViewModel", "Changing current conversation from $previousId to $conversationId")
        _currentConversationId.value = conversationId

        // If there's a new conversation opened, mark unread messages as read
        if (conversationId.isNotEmpty()) {
            markConversationMessagesAsRead(conversationId)
        }
    }

    private fun markConversationMessagesAsRead(conversationId: String) {
        val currentUserId = loginPreference.getUserId()
        val currentTime = System.currentTimeMillis()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Get all unread messages for this conversation
                val unreadMessages = messageRepository.getUnreadMessagesForConversation(
                    conversationId = conversationId,
                    currentUserId = currentUserId
                )

                Log.d("ConversationViewModel", "Found ${unreadMessages.size} unread messages to mark as read")

                if (unreadMessages.isNotEmpty()) {
                    // Update local database
                    unreadMessages.forEach { message ->
                        val status = RoomMessageStatus(
                            messageId = message.messageId,
                            userId = currentUserId,
                            readAt = currentTime,
                            receivedAt = currentTime
                        )
                        messageStatusRepository.addOrUpdateStatus(status)
                    }

                    // Update Firebase (batch update for efficiency)
                    val messageIds = unreadMessages.map { it.messageId }
                    firebaseMessageStatusRepository.markMessagesAsRead(
                        messageIds = messageIds,
                        userId = currentUserId,
                        timestamp = currentTime
                    )
                }else{
                    Log.d("ConversationViewModel", "No unread messages to mark as read")
                }
            } catch (e: Exception) {
                Log.e("ConversationViewModel", "Error marking messages as read", e)
            }
        }
    }
    private fun isConversationOpen(conversationId: String): Boolean {
        val isOpen = conversationId == _currentConversationId.value
        Log.d("ConversationViewModel", "Checking if conversation $conversationId is open: $isOpen (current=${_currentConversationId.value})")
        return isOpen
    }

    private fun deleteMessageFromLocalDatabase(messageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.deleteMessageById(messageId)
        }
    }

    fun clearLocalDatabase() {
        viewModelScope.launch {
            contactRepository.deleteAllContacts()
            conversationRepository.deleteAllConversations()
            messageRepository.deleteAllMessages()
            participantRepository.deleteAllParticipants()
            loginPreference.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        contactListenerRegistration?.remove()
        conversationListenerRegistration?.remove()
    }
}
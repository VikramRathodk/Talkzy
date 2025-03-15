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
import com.devvikram.talkzy.data.firebase.repository.FirebaseContactRepository
import com.devvikram.talkzy.data.firebase.repository.FirebaseMessageRepository
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
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
    private val firebaseMessageRepository: FirebaseMessageRepository

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
                            com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
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
                                com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                                    println("Document added: ${it.document.data}")
                                    conversationRepository.insertConversation(
                                        ModelMapper.toRoomConversation(
                                            conversation,
                                            loginPreference.getUserId()
                                        )
                                    )
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

                                com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                    println("Document modified: ${it.document.data}")

                                    conversationRepository.insertConversation(
                                        ModelMapper.toRoomConversation(
                                            conversation,
                                            loginPreference.getUserId()
                                        )
                                    )
                                }

                                com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                    println("Document removed: ${it.document.data}")
                                    conversationRepository.deleteConversation(conversation.conversationId)
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

    private fun syncMessages(conversationId: String) {
        Log.d(TAG, "syncMessages: Syncing Messages for conversation $conversationId")
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = formatter.format(Date())
        val userId = loginPreference.getUserId()

        // Remove existing listener before adding a new one
        messageListeners[conversationId]?.remove()

        messageListeners[conversationId] =
            firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
                .document(conversationId)
                .collection(currentDate)
                .orderBy("lastModifiedAt", Query.Direction.DESCENDING)
                // Remove limit to get all messages
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.e(TAG, "Error listening to messages: ${error.message}")
                        return@addSnapshotListener
                    }

                    snapshots?.documentChanges?.forEach { change ->
                        val message = change.document.toObject(ChatMessage::class.java)
                        Log.d(
                            TAG,
                            "Message sync event: ${change.type} for message ${message.messageId}"
                        )

                        viewModelScope.launch {
                            handleMessageSync(message, userId, change.type, currentDate)
                        }
                    }
                }
    }

    private suspend fun handleMessageSync(
        firebaseMessage: ChatMessage,
        userId: String,
        changeType: DocumentChange.Type,
        datePartition: String
    ) {
        Log.d(TAG, "handleMessageSync: ${firebaseMessage.messageId} - Type: $changeType")
        val currentTime = System.currentTimeMillis()

        when (changeType) {
            DocumentChange.Type.ADDED -> {
                handleAddedMessage(firebaseMessage, userId, currentTime, datePartition)
            }

            DocumentChange.Type.MODIFIED -> {
                Log.d(TAG, "Message Modified: ${firebaseMessage.messageId}")
                updateLocalDatabase(firebaseMessage)
            }

            DocumentChange.Type.REMOVED -> {
                Log.d(TAG, "Message Removed: ${firebaseMessage.messageId}")
                deleteMessageFromLocalDatabase(firebaseMessage.messageId)
            }
        }
    }

    private suspend fun handleAddedMessage(
        firebaseMessage: ChatMessage,
        userId: String,
        currentTime: Long,
        datePartition: String
    ) {
        Log.d(TAG, "handleAddedMessage: Adding ${firebaseMessage.messageId}")

        val readBy = firebaseMessage.isReadBy?.toMutableMap() ?: mutableMapOf()
        val receivedBy = firebaseMessage.isReceivedBy?.toMutableMap() ?: mutableMapOf()

        var isUpdated = false

        if (isConversationOpen(firebaseMessage.conversationId)) {
            if (!readBy.containsKey(userId)) {
                readBy[userId] = currentTime
                isUpdated = true
            }
        } else {
            if (!receivedBy.containsKey(userId)) {
                receivedBy[userId] = currentTime
                isUpdated = true
            }
        }

        if (isUpdated) {
            val updatedMessage = firebaseMessage.copy(
                isReadBy = readBy,
                isReceivedBy = receivedBy,
                lastModifiedAt = currentTime
            )

            messageRepository.updateReadField(
                messageId = updatedMessage.messageId,
                updatedMessage.isReadBy
            )
            messageRepository.updateReceivedField(
                messageId = updatedMessage.messageId,
                updatedMessage.isReceivedBy
            )
            messageRepository.updateLastModifiedAt(
                messageId = updatedMessage.messageId,
                currentTime
            )
            firebaseMessageRepository.updateMessageInFirebase(
                message = updatedMessage,
                datePartition = datePartition
            )
        }
    }

    private fun isConversationOpen(conversationId: String): Boolean {
        Log.d(TAG, "isConversationOpen: {${conversationId == _currentConversationId.value}")
        return conversationId == _currentConversationId.value
    }

    private suspend fun updateLocalDatabase(message: ChatMessage) {
        val existingMessage = messageRepository.getMessageByMessageId(message.messageId)

        if (existingMessage != null) {
            val updatedMessage = ModelMapper.mapToRoomMessage(
                chatMessage = message,
                existingMessage = existingMessage
            )


        } else {
            messageRepository.insertMessage(ModelMapper.mapToRoomMessage(message))
        }
    }

    private fun deleteMessageFromLocalDatabase(messageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.deleteMessageById(messageId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        contactListenerRegistration?.remove()
        conversationListenerRegistration?.remove()
    }
}
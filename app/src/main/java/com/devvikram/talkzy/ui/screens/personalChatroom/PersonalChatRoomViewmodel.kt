package com.devvikram.talkzy.ui.screens.personalChatroom

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.config.constants.MessageType
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.devvikram.talkzy.data.room.models.RoomParticipant
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonalChatRoomViewmodel @Inject constructor(
    @ApplicationContext context: Context,
    private val conversationRepository: ConversationRepository,
    private val loginPreference: LoginPreference,
    private val firebaseConversationRepository: FirebaseConversationRepository,
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _conversationId = MutableStateFlow("")
    val conversationId = _conversationId.asStateFlow()

    fun setConversationId(conversationId: String) {
        _conversationId.value = conversationId
    }

    private val _loggedUser = MutableStateFlow<RoomContact?>(null)
    val loggedUser: StateFlow<RoomContact?> = _loggedUser.asStateFlow()

    init {
        contactRepository.getContactByUserIdWithFlow(loginPreference.getUserId())
            .onEach { _loggedUser.value = it }
            .launchIn(viewModelScope)
    }


    private val _receiverUserProfile = MutableStateFlow<RoomContact?>(null)
    val receiverUserProfile: StateFlow<RoomContact?> get() = _receiverUserProfile


    fun sendMessage(message: String) {
        Log.d(TAG, "sendMessage called with message: $message")
        Log.d(TAG, "sendMessage: conversationId = ${conversationId}")

        viewModelScope.launch {
            try {
                val senderUserId = loginPreference.getUserId()
                val receiverUserId = _receiverUserProfile.value?.userId ?: run {
                    Log.e(TAG, "Receiver user profile is null. Cannot send message.")
                    return@launch
                }

                Log.d(TAG, "Sender User ID: $senderUserId, Receiver User ID: $receiverUserId")

                val existingConversation =
                    conversationRepository.getConversationByConversationId(_conversationId.value)
                val newMessageId =
                    firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
                        .document().id
                val participantIds = listOf(senderUserId, receiverUserId)

                if (existingConversation != null) {
                    sendMessageToExistingConversation(
                        existingConversation,
                        newMessageId,
                        message,
                        senderUserId
                    )
                } else {
                    createNewConversationAndSendMessage(
                        participantIds,
                        newMessageId,
                        message,
                        senderUserId,
                        receiverUserId
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in sendMessage: ${e.message}", e)
            }
        }
    }

    private suspend fun sendMessageToExistingConversation(
        existingConversation: RoomConversation,
        newMessageId: String,
        message: String,
        senderUserId: String
    ) {
        val newMessage = RoomMessage(
            messageId = newMessageId,
            conversationId = existingConversation.conversationId,
            text = message,
            senderId = senderUserId,
            senderName = _loggedUser.value?.name.orEmpty(),
            messageType = MessageType.TEXT.toString(),
            timestamp = System.currentTimeMillis(),
        )

        Log.d(TAG, "sendMessage: Existing conversation detected. Sending message: $newMessage")

        messageRepository.insertNewMessage(newMessage, existingConversation)
        Log.d(TAG, "sendMessage: Message sent successfully in existing conversation")
    }

    private suspend fun createNewConversationAndSendMessage(
        participantIds: List<String>,
        newMessageId: String,
        message: String,
        senderUserId: String,
        receiverUserId: String
    ) {
        val newConversationId =
            firestore.collection(FirebaseConstant.FIRESTORE_CONVERSATION_COLLECTION).document().id

        val newMessage = RoomMessage(
            messageId = newMessageId,
            conversationId = newConversationId,
            text = message,
            senderId = senderUserId,
            senderName = _loggedUser.value?.name.orEmpty(),
            messageType = MessageType.TEXT.toString(),
            timestamp = System.currentTimeMillis(),
        )

        val conversation = RoomConversation(
            conversationId = newConversationId,
            userId = receiverUserId,
            type = "P",
            name = _receiverUserProfile.value?.name.orEmpty(),
            createdBy = senderUserId,
            createdAt = System.currentTimeMillis(),
            participantIds = participantIds
        )

        val roomParticipants = participantIds.map {
            RoomParticipant(
                localParticipantId = 0,
                userId = it,
                conversationId = newConversationId,
                role = "MEMBER"
            )
        }

        Log.d(TAG, "Creating new conversation: $conversation")

        conversationRepository.createConversationWithMessage(
            conversation,
            roomParticipants,
            participantIds.map { Participant(it, "MEMBER") })
        messageRepository.insertNewMessage(newMessage, conversation)

        _conversationId.value = newConversationId
        Log.d(TAG, "sendMessage: New conversation created and message sent successfully")
    }

    fun getReceiverInfo(receiverId: String) {
        viewModelScope.launch {
            println("personal: getCurrentReceiver called ${receiverId}")
            val data = contactRepository.getContactById(receiverId)
            _receiverUserProfile.value = data
        }
    }

}
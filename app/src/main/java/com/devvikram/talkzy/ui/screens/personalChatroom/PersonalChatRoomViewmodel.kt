package com.devvikram.talkzy.ui.screens.personalChatroom

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.config.constants.MessageType
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.devvikram.talkzy.data.room.models.RoomParticipant
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
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
    private val messageRepository: MessageRepository
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
        println("sendMessage called with message: $message ")
        viewModelScope.launch {
            val senderUserId = loginPreference.getUserId()
            val existingConversation =
                conversationRepository.getConversationByConversationId(_conversationId.value)
            val receiverUserId = _receiverUserProfile.value?.userId.toString()
            val participantIds = listOf(senderUserId, receiverUserId)

            if (existingConversation != null) {
                // Step 1 : create a new message with message type text
                Log.d(
                    TAG,
                    "sendMessage: existingConversation: ${existingConversation.conversationId} name : ${existingConversation.name}"
                )
                val newMessage = RoomMessage(
                    messageId = "",
                    conversationId = existingConversation.conversationId,
                    text = message,
                    senderId = senderUserId,
                    senderName = _loggedUser.value?.name.toString(),
                    messageType = MessageType.TEXT.toString(),
                    timestamp = System.currentTimeMillis(),
                )
                Log.d(TAG, "sendMessage: Creating Existing message Without Message Id :$newMessage")

                messageRepository.insertNewMessage(
                    roomMessage = newMessage,
                    conversation = existingConversation,
                 )
            } else {

                val newMessage = RoomMessage(
                    messageId = "",
                    conversationId = "",
                    text = message,
                    senderName = _loggedUser.value?.name.toString(),
                    senderId = senderUserId,
                    messageType = MessageType.TEXT.toString(),
                    timestamp = System.currentTimeMillis(),
                )
                Log.d(TAG, "sendMessage: Creating New message Without Message Id :$newMessage")

                val conversation = RoomConversation(
                    conversationId = "",
                    userId = receiverUserId,
                    type = "P",
                    name = _receiverUserProfile.value?.name.toString(),
                    createdBy = senderUserId,
                    createdAt = System.currentTimeMillis(),
                    participantIds = participantIds
                )

                val roomParticipants = participantIds.map {
                    RoomParticipant(
                        localParticipantId = 0,
                        userId = it,
                        conversationId = conversation.conversationId,
                        role = "MEMBER"
                    )
                }
                val participants = participantIds.map { Participant(userId = it, role = "MEMBER") }

                println("personal: conversation not exits creating new conversation $conversation")
                conversationRepository.createConversationWithMessage(
                    conversation,
                    roomParticipants,
                    participants,
                    newMessage
                )
            }

        }
    }

    fun getReceiverInfo(receiverId: String) {
        viewModelScope.launch {
            println("personal: getCurrentReceiver called ${receiverId}")
            val data = contactRepository.getContactById(receiverId)
            _receiverUserProfile.value = data
        }
    }

}
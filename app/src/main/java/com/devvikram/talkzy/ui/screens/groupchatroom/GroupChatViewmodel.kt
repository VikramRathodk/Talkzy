package com.devvikram.talkzy.ui.screens.groupchatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.config.constants.MessageType
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.room.models.ConversationWithContacts
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatViewmodel @Inject constructor(
    private val loginPreference: LoginPreference,
    private val firebaseConversationRepository: FirebaseConversationRepository,
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository,
    private val firestore: FirebaseFirestore,
    private val conversationRepository: ConversationRepository
) : ViewModel() {


    private val _conversationId = MutableStateFlow("")
    val conversationId = _conversationId.asStateFlow()

    fun setConversationId(conversationId: String) {
        _conversationId.value = conversationId
    }

    internal val _isTyping = MutableStateFlow<Boolean>(false)

    private val _chatMessageList = MutableStateFlow<List<GroupChatMessageItem>>(emptyList())
    val chatMessageList: StateFlow<List<GroupChatMessageItem>> get() = _chatMessageList.asStateFlow()

    private val _loggedUser = MutableStateFlow<RoomContact?>(null)
    val loggedUser: StateFlow<RoomContact?> = _loggedUser.asStateFlow()


    private val _groupInformation = MutableStateFlow<ConversationWithContacts?>(null)
    val groupInformation: StateFlow<ConversationWithContacts?> = _groupInformation.asStateFlow()


    init {
        viewModelScope.launch {
            _conversationId.collectLatest { id ->
                if (id.isNotBlank()) {
                    launch { observeConversationInfo(id) }
                    launch { observeMessages() }
                }
            }
        }
        contactRepository.getContactByUserIdWithFlow(loginPreference.getUserId())
            .onEach { _loggedUser.value = it }
            .launchIn(viewModelScope)


    }

    private suspend fun observeConversationInfo(conversationId : String){
        conversationRepository.getConversationByConversationIdFlow(_conversationId.value).collectLatest {
            _groupInformation.value = it
        }
    }
    private suspend fun observeMessages() {

        messageRepository.getMessageByConversationIdWithFlow(_conversationId.value)
            .collectLatest { messages ->
                _chatMessageList.value = messages.mapNotNull { roomMessage ->
                    when (roomMessage.messageType) {
                        MessageType.TEXT.name -> {
                            if (roomMessage.senderId == loginPreference.getUserId()) {
                                GroupChatMessageItem.SenderTextMessageItem(
                                    messageId = roomMessage.messageId,
                                    conversationId = roomMessage.conversationId,
                                    senderId = roomMessage.senderId,
                                    senderName = roomMessage.senderName ?: "Unknown",
                                    text = roomMessage.text.orEmpty(),
                                    timestamp = roomMessage.timestamp,
                                    isEdited = roomMessage.isEdited,
                                    replyToMessageId = roomMessage.replyToMessageId,
                                    isReadBy = roomMessage.isReadBy,
                                    isReceivedBy = roomMessage.isReceivedBy
                                )
                            } else {
                                GroupChatMessageItem.ReceiverTextMessageItem(
                                    messageId = roomMessage.messageId,
                                    conversationId = roomMessage.conversationId,
                                    senderId = roomMessage.senderId,
                                    senderName = roomMessage.senderName ?: "Unknown",
                                    text = roomMessage.text.orEmpty(),
                                    timestamp = roomMessage.timestamp,
                                    isEdited = roomMessage.isEdited,
                                    replyToMessageId = roomMessage.replyToMessageId,
                                    isReadBy = roomMessage.isReadBy,
                                    isReceivedBy = roomMessage.isReceivedBy
                                )
                            }
                        }

                        MessageType.IMAGE.name -> {
                            if (roomMessage.senderId == loginPreference.getUserId()) {
                                GroupChatMessageItem.SenderImageMessageItem(
                                    messageId = roomMessage.messageId,
                                    conversationId = roomMessage.conversationId,
                                    senderId = roomMessage.senderId,
                                    senderName = roomMessage.senderName ?: "Unknown",
                                    imageUrl = roomMessage.mediaUrl.orEmpty(),
                                    timestamp = roomMessage.timestamp,
                                    mediaSize = roomMessage.mediaSize,
                                    thumbnailUrl = roomMessage.thumbnailUrl,
                                    isUploaded = roomMessage.isUploaded,
                                    isDownloaded = roomMessage.isDownloaded,
                                    isReadBy = roomMessage.isReadBy,
                                    isReceivedBy = roomMessage.isReceivedBy
                                )
                            } else {
                                GroupChatMessageItem.ReceiverImageMessageItem(
                                    messageId = roomMessage.messageId,
                                    conversationId = roomMessage.conversationId,
                                    senderId = roomMessage.senderId,
                                    senderName = roomMessage.senderName ?: "Unknown",
                                    imageUrl = roomMessage.mediaUrl.orEmpty(),
                                    timestamp = roomMessage.timestamp,
                                    mediaSize = roomMessage.mediaSize,
                                    thumbnailUrl = roomMessage.thumbnailUrl,
                                    isUploaded = roomMessage.isUploaded,
                                    isDownloaded = roomMessage.isDownloaded,
                                    isReadBy = roomMessage.isReadBy,
                                    isReceivedBy = roomMessage.isReceivedBy
                                )
                            }
                        }

                        else -> if (_isTyping.value) GroupChatMessageItem.TypingIndicator(
                            userId = roomMessage.senderId,
                            userName = roomMessage.senderName ?: "Unknown"
                        ) else null
                    }
                }

            }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val existingConversation =  conversationRepository.getConversationByConversationId(
                _conversationId.value
            )
            val newMessageId =
                firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
                    .document().id
            val senderUserId = loginPreference.getUserId()

            if(existingConversation != null){
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
            }
        }
    }
}
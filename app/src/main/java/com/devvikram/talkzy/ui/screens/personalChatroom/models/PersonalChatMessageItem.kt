package com.devvikram.talkzy.ui.screens.personalChatroom.models

sealed class PersonalChatMessageItem {

    // Sender's Text Message
    data class SenderTextMessageItem(
        val messageId: String,
        val conversationId: String,
        val senderId: String,
        val senderName: String,
        val text: String,
        val timestamp: Long,
        val isEdited: Boolean = false,
        val replyToMessageId: String? = null,
        val isReadBy: Map<String, Long> = emptyMap(),
        val isReceivedBy: Map<String, Long> = emptyMap(),
    ) : PersonalChatMessageItem()

    // Receiver's Text Message
    data class ReceiverTextMessageItem(
        val messageId: String,
        val conversationId: String,
        val senderId: String,
        val senderName: String,
        val text: String,
        val timestamp: Long,
        val isEdited: Boolean = false,
        val replyToMessageId: String? = null,
        val isReadBy: Map<String, Long> = emptyMap(),
        val isReceivedBy: Map<String, Long> = emptyMap(),
    ) : PersonalChatMessageItem()

    // Sender's Image Message
    data class SenderImageMessageItem(
        val messageId: String,
        val conversationId: String,
        val senderId: String,
        val senderName: String,
        val imageUrl: String,
        val timestamp: Long,
        val mediaSize: Long? = null,
        val thumbnailUrl: String? = null,
        val isUploaded: Boolean = false,
        val isDownloaded: Boolean = false,
        val isReadBy: Map<String, Long> = emptyMap(),
        val isReceivedBy: Map<String, Long> = emptyMap(),
    ) : PersonalChatMessageItem()

    // Receiver's Image Message
    data class ReceiverImageMessageItem(
        val messageId: String,
        val conversationId: String,
        val senderId: String,
        val senderName: String,
        val imageUrl: String,
        val timestamp: Long,
        val mediaSize: Long? = null,
        val thumbnailUrl: String? = null,
        val isUploaded: Boolean = false,
        val isDownloaded: Boolean = false,
        val isReadBy: Map<String, Long> = emptyMap(),
        val isReceivedBy: Map<String, Long> = emptyMap(),
    ) : PersonalChatMessageItem()

    // Typing Indicator (Shown for Receiver)
    data object TypingIndicator : PersonalChatMessageItem()
}


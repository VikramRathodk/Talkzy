package com.devvikram.talkzy.ui.screens.groupchatroom

sealed class GroupChatMessageItem {

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
        val isReceivedBy: Map<String, Long> = emptyMap()
    ) : GroupChatMessageItem()

    // Receiver's Text Message
    data class ReceiverTextMessageItem(
        val messageId: String,
        val conversationId: String,
        val senderId: String,
        val senderName: String,
        val senderAvatarUrl: String? = null,
        val text: String,
        val timestamp: Long,
        val isEdited: Boolean = false,
        val replyToMessageId: String? = null,
        val isReadBy: Map<String, Long> = emptyMap(),
        val isReceivedBy: Map<String, Long> = emptyMap()
    ) : GroupChatMessageItem()

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
        val isReceivedBy: Map<String, Long> = emptyMap()
    ) : GroupChatMessageItem()

    // Receiver's Image Message
    data class ReceiverImageMessageItem(
        val messageId: String,
        val conversationId: String,
        val senderId: String,
        val senderName: String,
        val senderAvatarUrl: String? = null,
        val imageUrl: String,
        val timestamp: Long,
        val mediaSize: Long? = null,
        val thumbnailUrl: String? = null,
        val isUploaded: Boolean = false,
        val isDownloaded: Boolean = false,
        val isReadBy: Map<String, Long> = emptyMap(),
        val isReceivedBy: Map<String, Long> = emptyMap()
    ) : GroupChatMessageItem()

    // Typing Indicator
    data class TypingIndicator(
        val userId: String,
        val userName: String
    ) : GroupChatMessageItem()
}

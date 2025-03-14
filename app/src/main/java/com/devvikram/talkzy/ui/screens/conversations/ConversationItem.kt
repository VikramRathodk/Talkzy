package com.devvikram.talkzy.ui.screens.conversations

import com.devvikram.talkzy.data.room.models.LastMessage


sealed class ConversationItem {
    abstract val conversationId: String
    abstract val unreadMessageCount: Int
    abstract var selected: Boolean
    abstract val timeStamp: Long
    abstract val lastMessage : LastMessage

    data class PersonalConversation(
        override val conversationId: String = "",
        val userId: String = "",
        val name: String = "",
        override val unreadMessageCount: Int = 0,
        val localProfilePicPath: String = "",
        override var selected: Boolean = false,
        val isForwarded: Boolean = false,
        override val timeStamp: Long = System.currentTimeMillis(),
        override val lastMessage: LastMessage
    ) : ConversationItem()

    data class GroupConversation(
        val groupTitle: String,
        override val conversationId: String,
        val groupIconUrl: String = "",
        val groupIconLocalPath: String = "",
        override val unreadMessageCount: Int,
        override var selected: Boolean,
        val participants: List<String> = arrayListOf(),
        val createdBy : String = "",
        override val timeStamp: Long,
        override val lastMessage: LastMessage
    ) : ConversationItem()
}

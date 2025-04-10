package com.devvikram.talkzy.data.firebase.models

data class MessageStatus(
    val messageId: String = "",
    val userId: String = "",
    val receivedAt: Long? = null,
    val readAt: Long? = null
)


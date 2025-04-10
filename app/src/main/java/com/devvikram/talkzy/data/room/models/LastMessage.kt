package com.devvikram.talkzy.data.room.models

data class LastMessage(
    val text: String  ="",
    val type: String = "",
    val senderId: String    = "",
    val timestamp: Long,
    val mediaUrl: String? = null,
    val mediaType: String? = null,
    val conversationId: String = ""
)

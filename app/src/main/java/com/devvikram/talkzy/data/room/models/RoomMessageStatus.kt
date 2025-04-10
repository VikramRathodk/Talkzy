package com.devvikram.talkzy.data.room.models

import androidx.room.Entity


@Entity(
    tableName = "message_status",
    primaryKeys = ["messageId", "userId"]
)
data class RoomMessageStatus(
    val messageId: String,
    val userId: String,
    val receivedAt: Long? = null,
    val readAt: Long? = null
)
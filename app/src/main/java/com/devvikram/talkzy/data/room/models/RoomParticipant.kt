package com.devvikram.talkzy.data.room.models

import androidx.room.Entity

@Entity(
    tableName = "participants",
    primaryKeys = ["conversationId", "userId"]
)
data class RoomParticipant(
    val conversationId: String,
    val userId: String,
    val role: String? = null,
    val localParticipantId: Long = 0L
)

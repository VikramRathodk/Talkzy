package com.devvikram.talkzy.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class RoomParticipant(
    @PrimaryKey(autoGenerate = true)
    val localParticipantId: Long,
    val userId: String,//unknown
    val conversationId: String, // Foreign key reference to conversation
    var role: String? = null // Role of the participant (e.g., "admin", "member")
)
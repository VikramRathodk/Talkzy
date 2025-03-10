package com.devvikram.talkzy.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class RoomConversation(
    @PrimaryKey val conversationId: String,
    val userId: String,
    val type: String, // "P" (personal), "G" (group)
    val groupType: String? = null, // 1.General Group 2. Specific Group
    val name: String? = null, // Optional, for group chats
    val createdBy: String,
    val createdAt: Long, // Store timestamp as epoch milliseconds
    val description: String? = null, // Group chat description // Key: userId, Value: banned status
    val participantIds: List<String> = emptyList(), // List of participant IDs
    val lastModifiedAt: Long = System.currentTimeMillis()
)
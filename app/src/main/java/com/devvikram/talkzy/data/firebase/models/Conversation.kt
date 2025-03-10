package com.devvikram.talkzy.data.firebase.models

data class Conversation(
    var conversationId: String = "",
    val type: String ,    // P  (Personal Conversation), G (Group Conversation)
    val groupType: String? = null, // 1.General Group 2. Specific Group
    val name: String? = null, // Optional, for group chats
    val createdBy: String,   // User ID of the creator
    val createdAt: Long,
    val description: String? = null,  // Group chat description
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val participantIds : List<String> = emptyList(), // List of participant IDs
    val participants: List<Participant> = emptyList(),
)
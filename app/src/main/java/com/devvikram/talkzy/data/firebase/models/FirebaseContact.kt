package com.devvikram.talkzy.data.firebase.models

import com.devvikram.talkzy.config.enums.ContactType

data class FirebaseContact(
    val userId: String = "", // user id of the contact
    val name: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val profilePicture: String = "",
    val conversationId: String = "",
    val contactType: String = ContactType.PERSONAL.name,
    val isOnline: Boolean = false,
    val lastSeen: Long = 0L,
    val status: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis()
)
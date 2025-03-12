package com.devvikram.talkzy.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devvikram.talkzy.config.enums.ContactType

@Entity(tableName = "contacts")
data class RoomContact(
    val localContactId: Long? = 0L,
    @PrimaryKey
    val userId: String = "", // user id of the contact
    val name: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val profilePicture: String = "",
    val localProfilePicturePath: String = "",
    val conversationId: String = "",
    val contactType: String = ContactType.PERSONAL.name,
    val isOnline: Boolean = false,
    val lastSeen: Long = 0L,
    val status: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis(),

    )
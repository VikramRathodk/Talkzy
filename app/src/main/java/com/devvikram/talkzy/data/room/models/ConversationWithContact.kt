package com.devvikram.talkzy.data.room.models

import androidx.room.Embedded
import androidx.room.Relation


data class ConversationWithContacts(
    @Embedded val conversation: RoomConversation,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val contact: RoomContact? = null
)

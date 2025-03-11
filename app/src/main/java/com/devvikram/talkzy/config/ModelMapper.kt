package com.devvikram.talkzy.config

import com.devvikram.talkzy.config.enums.ContactType
import com.devvikram.talkzy.data.firebase.models.Conversation
import com.devvikram.talkzy.data.firebase.models.FirebaseContact
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomParticipant


object ModelMapper {

    // Convert RoomContact to FirebaseContact
    fun toFirebaseContact(roomContact: RoomContact): FirebaseContact {
        return FirebaseContact(
            userId = roomContact.userId,
            name = roomContact.name,
            email = roomContact.email,
            mobileNumber = roomContact.mobileNumber,
            profilePicture = roomContact.profilePicture,
            conversationId = roomContact.conversationId,
            contactType = roomContact.contactType,
            isOnline = roomContact.isOnline,
            lastSeen = roomContact.lastSeen,
            status = roomContact.status,
            createdAt = roomContact.createdAt,
            lastModifiedAt = roomContact.lastModifiedAt
        )
    }

    // Convert FirebaseContact to RoomContact
    fun toRoomContact(firebaseContact: FirebaseContact): RoomContact {
        return RoomContact(
            localContactId = 0L,
            userId = firebaseContact.userId,
            name = firebaseContact.name,
            email = firebaseContact.email,
            mobileNumber = firebaseContact.mobileNumber,
            profilePicture = firebaseContact.profilePicture,
            conversationId = firebaseContact.conversationId,
            contactType = firebaseContact.contactType,
            isOnline = firebaseContact.isOnline,
            lastSeen = firebaseContact.lastSeen,
            status = firebaseContact.status,
            createdAt = firebaseContact.createdAt,
            lastModifiedAt = firebaseContact.lastModifiedAt
        )
    }


    // Convert Firebase Conversation to RoomConversation
    fun toRoomConversation(conversation: Conversation, userId: String): RoomConversation {
        return RoomConversation(
            conversationId = conversation.conversationId,
            userId = userId,
            type = conversation.type,
            groupType = conversation.groupType,
            name = conversation.name,
            createdBy = conversation.createdBy,
            createdAt = conversation.createdAt,
            description = conversation.description,
            participantIds = conversation.participantIds,
            lastModifiedAt = conversation.lastModifiedAt
        )
    }

    // Convert RoomConversation to Firebase Conversation
    fun toFirebaseConversation(roomConversation: RoomConversation): Conversation {
        return Conversation(
            conversationId = roomConversation.conversationId,
            type = roomConversation.type,
            groupType = roomConversation.groupType,
            name = roomConversation.name,
            createdBy = roomConversation.createdBy,
            createdAt = roomConversation.createdAt,
            description = roomConversation.description,
            lastModifiedAt = roomConversation.lastModifiedAt,
            participantIds = roomConversation.participantIds,
        )
    }

    // Convert Firebase Participant to RoomParticipant
    fun toRoomParticipant(participant: Participant, conversationId: String): RoomParticipant {
        return RoomParticipant(
            localParticipantId = 0,
            userId = participant.userId,
            conversationId = conversationId,
            role = participant.role
        )
    }

    // Convert RoomParticipant to Firebase Participant
    fun toFirebaseParticipant(roomParticipant: RoomParticipant): Participant {
        return Participant(
            userId = roomParticipant.userId,
            role = roomParticipant.role ?: "MEMBER"
        )
    }
}
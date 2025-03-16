package com.devvikram.talkzy.config

import com.devvikram.talkzy.config.constants.MessageType
import com.devvikram.talkzy.data.firebase.models.ChatMessage
import com.devvikram.talkzy.data.firebase.models.Conversation
import com.devvikram.talkzy.data.firebase.models.FirebaseContact
import com.devvikram.talkzy.data.firebase.models.ForwardMetadata
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomForwardMetadata
import com.devvikram.talkzy.data.room.models.RoomMessage
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
    fun toRoomConversation(conversation: Conversation, currentUserId: String): RoomConversation {
        val userId = conversation.participantIds.firstOrNull { it != currentUserId }
            ?: currentUserId

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
    fun toFirebaseConversation(roomConversation: RoomConversation, participants: List<Participant>): Conversation {
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
            participants = participants
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

    // 3. ChatMessage to RoomMessage
    fun mapToRoomMessage(chatMessage: ChatMessage, existingMessage: RoomMessage? = null): RoomMessage {
        return RoomMessage(
            messageId = chatMessage.messageId,
            conversationId = chatMessage.conversationId,
            senderId = chatMessage.senderId,
            senderName = chatMessage.senderName,
            messageType = chatMessage.messageType.name,
            text = chatMessage.text,
            timestamp = chatMessage.timestamp ?: 0L,
            mediaUrl = chatMessage.mediaUrl,
            thumbnailUrl = chatMessage.thumbnailUrl,
            mediaSize = chatMessage.mediaSize ?: existingMessage?.mediaSize,
            mediaDurationInSeconds = chatMessage.mediaDurationInSeconds,
            isEdited = chatMessage.isEdited,
            reactions = chatMessage.reactions.mapValues { it.value },
            isReadBy = chatMessage.isReadBy.mapValues { it.value ?: 0L },
            isReceivedBy = chatMessage.isReceivedBy.mapValues { it.value ?: 0L },
            mentions = chatMessage.mentions,
            replyToMessageId = chatMessage.replyToMessageId,
            deletedForUsers = chatMessage.deletedForUsers,
            isDeleted = chatMessage.isDeleted,
            isForwarded = chatMessage.isForwarded,
            forwardMetadata = chatMessage.forwardMetadata?.let {
                RoomForwardMetadata(
                    originalMessageId = it.originalMessageId,
                    originalSenderId = it.originalSenderId,
                    originalSenderName = it.originalSenderName,
                    originalConversationId = it.originalConversationId,
                    originalTimestamp = it.originalTimestamp ?: 0L,
                    forwarderChain = it.forwarderChain
                )
            },
            forwardCount = chatMessage.forwardCount,
            localFilePath = null,
            isDownloaded = false,
            mediaType = chatMessage.mediaType,
            mediaName = chatMessage.mediaName,
            mediaLocalUrl = existingMessage?.mediaLocalUrl,
            mediaStatus = existingMessage?.mediaStatus.toString(),
            uploadProgress = existingMessage?.uploadProgress ?: 0,
            isUploaded = chatMessage.isUploaded,
            datePartition = chatMessage.datePartition,
            lastModifiedAt = chatMessage.lastModifiedAt

            )
    }

    // 4. RoomMessage to ChatMessage
    fun mapToChatMessage(roomMessage: RoomMessage): ChatMessage {
        return ChatMessage(
            messageId = roomMessage.messageId,
            conversationId = roomMessage.conversationId,
            senderId = roomMessage.senderId,
            senderName = roomMessage.senderName,
            messageType = MessageType.valueOf(roomMessage.messageType),
            text = roomMessage.text,
            timestamp = roomMessage.timestamp,
            mediaUrl = roomMessage.mediaUrl,
            thumbnailUrl = roomMessage.thumbnailUrl,
            mediaSize = roomMessage.mediaSize,
            mediaDurationInSeconds = roomMessage.mediaDurationInSeconds,
            isEdited = roomMessage.isEdited,
            reactions = roomMessage.reactions.mapValues { it.value },
            isReadBy = roomMessage.isReadBy.mapValues { it.value ?: 0L },
            isReceivedBy = roomMessage.isReceivedBy.mapValues { it.value ?: 0L },
            mentions = roomMessage.mentions,
            replyToMessageId = roomMessage.replyToMessageId,
            deletedForUsers = roomMessage.deletedForUsers,
            isDeleted = roomMessage.isDeleted,
            isForwarded = roomMessage.isForwarded,
            forwardMetadata = roomMessage.forwardMetadata?.let {
                ForwardMetadata(
                    originalMessageId = it.originalMessageId,
                    originalSenderId = it.originalSenderId,
                    originalSenderName = it.originalSenderName,
                    originalConversationId = it.originalConversationId,
                    originalTimestamp = it.originalTimestamp,
                    forwarderChain = it.forwarderChain
                )
            },
            forwardCount = roomMessage.forwardCount,
            mediaType = roomMessage.mediaType,
            mediaName = roomMessage.mediaName,
            datePartition = roomMessage.datePartition,
            lastModifiedAt = roomMessage.lastModifiedAt
        )
    }
}
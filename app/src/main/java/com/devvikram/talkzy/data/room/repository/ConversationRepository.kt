package com.devvikram.talkzy.data.room.repository

import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.models.ConversationWithContacts
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.devvikram.talkzy.data.room.models.RoomParticipant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.collections.map

class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageRepository: MessageRepository,
    private val participantRepository: ParticipantRepository,
    private val firebaseConversationRepository: FirebaseConversationRepository,
    private val firestore: FirebaseFirestore
) {

    suspend fun createConversationWithMessage(
        conversation: RoomConversation,
        roomParticipants: List<RoomParticipant>,
        participants: List<Participant>,
    ) {
        conversationDao.insertConversation(conversation)
        participantRepository.insertAllParticipants(roomParticipants)
        firebaseConversationRepository.insertConversation(
            ModelMapper.toFirebaseConversation(
                roomConversation = conversation,
                participants = participants
            )
        )
    }


    suspend fun insertConversation(conversation: RoomConversation) {
        conversationDao.insertConversation(conversation)
    }

    suspend fun getConversationByConversationId(conversationId: String): RoomConversation {
        return conversationDao.getConversationByConversationId(conversationId)
    }

    suspend fun deleteConversation(conversationId: String) {
        conversationDao.deleteConversation(conversationId)

    }

    fun getConversationWithContactFlow(): Flow<List<ConversationWithContacts>> {
        return conversationDao.getConversationWithContactFlow()
    }

   suspend fun deleteAllConversations() {
       conversationDao.deleteAllConversations()
   }


}
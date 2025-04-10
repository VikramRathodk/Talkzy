package com.devvikram.talkzy.data.room.repository

import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.models.ConversationWithContacts
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomParticipant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val participantRepository: ParticipantRepository,
    private val firebaseConversationRepository: FirebaseConversationRepository,
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
    fun getConversationByConversationIdFlow(conversationId: String): Flow<ConversationWithContacts> {
        return conversationDao.getConversationByConversationIdFlow(conversationId)
    }

    suspend fun deleteConversation(conversationId: String) {
        conversationDao.deleteConversation(conversationId)

    }

    fun getAllConversationWithContactFlow(): Flow<List<ConversationWithContacts>> {
        return conversationDao.getConversationWithContactFlow()
    }

   suspend fun deleteAllConversations() {
       conversationDao.deleteAllConversations()
   }
}
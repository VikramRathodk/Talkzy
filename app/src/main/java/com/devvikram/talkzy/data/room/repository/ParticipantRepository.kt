package com.devvikram.talkzy.data.room.repository

import com.devvikram.talkzy.data.room.dao.ParticipantDao
import com.devvikram.talkzy.data.room.models.RoomParticipant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ParticipantRepository @Inject constructor(
    private val participantDao: ParticipantDao
) {

    suspend fun insertParticipant(participant: RoomParticipant) {
        participantDao.insertParticipant(participant)
    }

    suspend fun insertAllParticipants(participants: List<RoomParticipant>) {
        participantDao.insertAllParticipants(participants)
    }


    suspend fun getAllParticipants(conversationId: String) =
        participantDao.getAllParticipants(conversationId)

    suspend fun deleteAllParticipants() {
        participantDao.deleteAllParticipants()

    }

    suspend fun deleteParticipantsByConversationId(conversationId: String)  {
        participantDao.deleteParticipantsByConversationId(conversationId)
    }
}


package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.talkzy.data.room.models.RoomParticipant

@Dao
interface ParticipantDao {

    @Upsert
    suspend fun insertParticipant(participant: RoomParticipant)

    @Upsert
    suspend fun insertAllParticipants(participants: List<RoomParticipant>)

    @Query("SELECT * FROM participants WHERE conversationId = :conversationId")
    suspend fun getAllParticipants(
        conversationId: String
    ): List<RoomParticipant>



}
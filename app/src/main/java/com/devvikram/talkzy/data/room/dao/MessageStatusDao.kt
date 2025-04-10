package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.talkzy.data.room.models.RoomMessageStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageStatusDao {

    @Upsert
    suspend fun insertOrUpdate(status: RoomMessageStatus)

    @Query("SELECT * FROM message_status WHERE messageId = :messageId")
    fun getStatusesForMessage(messageId: String): Flow<List<RoomMessageStatus>>

    @Query("SELECT * FROM message_status WHERE userId = :userId")
    suspend fun getStatusesByUser(userId: String): List<RoomMessageStatus>

    @Query("SELECT * FROM message_status WHERE userId = :userId")
    suspend fun getStatusesByMessageIdAndUserId(userId: String): List<RoomMessageStatus>

    @Query("Delete from message_status WHERE messageId = :messageId")
    suspend fun deleteStatusesForMessage(messageId: String)

    @Query("DELETE FROM message_status WHERE messageId = :messageId")
    suspend fun deleteStatusByMessageId(messageId: String)

    @Query("SELECT * FROM message_status WHERE messageId = :messageId")
    suspend fun getReadByFromMessaegeId(messageId: String): List<RoomMessageStatus>

    @Query("SELECT * FROM message_status WHERE messageId = :messageId")
    suspend fun getReceivedByMesssageId(messageId: String): List<RoomMessageStatus>
}

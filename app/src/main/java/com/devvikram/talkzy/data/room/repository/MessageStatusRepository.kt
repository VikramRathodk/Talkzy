package com.devvikram.talkzy.data.room.repository

import androidx.room.Room
import com.devvikram.talkzy.data.room.dao.MessageStatusDao
import com.devvikram.talkzy.data.room.models.RoomMessageStatus
import kotlinx.coroutines.flow.Flow

class MessageStatusRepository(
    private val messageStatusDao: MessageStatusDao
) {

    suspend fun addOrUpdateStatus(status: RoomMessageStatus) {
        messageStatusDao.insertOrUpdate(status)
    }

    suspend fun getStatusesForMessageWithFlow(messageId: String): Flow<List<RoomMessageStatus>> {
        return messageStatusDao.getStatusesForMessage(messageId)
    }
    suspend fun getReadByFromMessaegeId(messageId: String): List<RoomMessageStatus> {
        return messageStatusDao.getReadByFromMessaegeId(messageId)
    }
    suspend fun getReceivedByMesssageId(messageId: String): List<RoomMessageStatus> {
        return messageStatusDao.getReceivedByMesssageId(messageId)
    }

    suspend fun getStatusesByUser(userId: String): List<RoomMessageStatus> {
        return messageStatusDao.getStatusesByUser(userId)

    }
    suspend fun deleteStatusesForMessage(messageId: String) {
        messageStatusDao.deleteStatusesForMessage(messageId)
    }

    suspend fun deleteStatusByMessageId(messageId: String){
        messageStatusDao.deleteStatusByMessageId(messageId)
    }
}

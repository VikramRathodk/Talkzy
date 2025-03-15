package com.devvikram.talkzy.data.room.repository

import android.content.Context
import android.util.Log
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.data.firebase.repository.FirebaseMessageRepository
import com.devvikram.talkzy.data.room.dao.MessageDao
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val messageDao: MessageDao,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun insertNewMessage(
        roomMessage: RoomMessage,
        conversation: RoomConversation,
    ) {
        try {
            val datePartition = AppUtils.getDatePartition(System.currentTimeMillis())
            Log.d("InsertNewMessage", "Calculated date partition: $datePartition")

            val newMessage = roomMessage.copy(datePartition = datePartition)
            Log.d("InsertNewMessage", "New message object: $newMessage")

            // Insert into local database
            messageDao.insertMessage(newMessage)
            Log.d("InsertNewMessage", "Message inserted into local database")

            // Insert into Firestore
            firebaseMessageRepository.insertMessage(
                datePartition = datePartition,
                conversationId = conversation.conversationId,
                message = ModelMapper.mapToChatMessage(newMessage),
            )
            Log.d("InsertNewMessage", "Message inserted into Firestore successfully")

        } catch (e: Exception) {
            Log.e("InsertNewMessage", "Error inserting message: ${e.message}", e)
        }
    }

    fun getMessageByConversationIdWithFlow(conversationId: String): Flow<List<RoomMessage>> {
        return messageDao.getMessagesByConversationIdWithFlow(conversationId)
    }

    internal suspend fun insertMessage(message: RoomMessage) {
        messageDao.insertMessage(message)
    }

    suspend fun getMessageByMessageId(messageId: String): RoomMessage {
        return messageDao.getMessageByMessageId(messageId)
    }

    suspend fun updateReceivedField(
        messageId: String,
        updatedReceivedBy: Map<String, Long>,
    ) {
        messageDao.updateMessageReceivedByMap(messageId, updatedReceivedBy.toMap())

    }

    suspend fun updateReadField(
        messageId: String,
        updatedReadBy: Map<String, Long>,
    ) {
        messageDao.updateMessageReadBy(messageId, updatedReadBy.toMap())
    }

    suspend fun updateLastModifiedAt(messageId: String, currentTime: Long) {
        messageDao.updateLastModifiedAt(messageId, currentTime)

    }
    suspend fun deleteMessageById(messageId: String) {
        messageDao.deleteMessageById(messageId)
    }
}
package com.devvikram.talkzy.data.room.repository

import android.content.Context
import android.util.Log
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.firebase.repository.FirebaseMessageRepository
import com.devvikram.talkzy.data.room.dao.MessageDao
import com.devvikram.talkzy.data.room.models.LastMessage
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    @ApplicationContext private val context: Context,
    private val loginPreference: LoginPreference
) {
    suspend fun insertNewMessage(
        roomMessage: RoomMessage,
    ) {
        try {
            // Insert into local database
            messageDao.insertMessage(roomMessage)
            Log.d("InsertNewMessage", "Message inserted into local database")
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

    suspend fun updateLastModifiedAt(messageId: String, currentTime: Long) {
        messageDao.updateLastModifiedAt(messageId, currentTime)

    }

    suspend fun deleteMessageById(messageId: String) {
        messageDao.deleteMessageById(messageId)
    }

    suspend fun deleteAllMessages() {
        messageDao.deleteAllMessages()

    }

    fun getLastMessageFlow(conversationId: String): Flow<RoomMessage?> {
        return messageDao.getLastMessageWithFlow(conversationId)
    }

    suspend fun getMessageById(messageId: String): RoomMessage? {
        return messageDao.getMessageById(messageId)
    }

    suspend  fun getMessageIdsForConversation(conversationId: String) : List<String> {
       return messageDao.getMessageIdsForConversation(conversationId)
    }
    // In MessageRepository.kt
    suspend fun getUnreadMessagesForConversation(conversationId: String, currentUserId: String): List<RoomMessage> {
        return messageDao.getUnreadMessagesForConversation(conversationId, currentUserId)
    }
}
package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.talkzy.data.room.models.LastMessage
import com.devvikram.talkzy.data.room.models.RoomMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Upsert
    suspend fun insertMessage(message: RoomMessage)

    @Upsert
    suspend fun insertMessages(messages: List<RoomMessage>)

    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): RoomMessage?

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId")
    fun getMessagesByConversationIdWithFlow(conversationId: String): Flow<List<RoomMessage>>

    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageByMessageId(messageId: String): RoomMessage


    @Query("UPDATE messages SET lastModifiedAt = :currentTime WHERE messageId = :messageId")
    suspend fun updateLastModifiedAt(messageId: String, currentTime: Long)

    @Query("DELETE FROM messages WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT 1")
    fun getLastMessageWithFlow(conversationId: String): Flow<RoomMessage?>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND senderId != :userId")
    suspend fun getAllMessagesInConversation(conversationId: String, userId: String): List<RoomMessage>

    @Query("SELECT messageId FROM messages WHERE conversationId = :conversationId")
    suspend fun getMessageIdsForConversation(conversationId: String) : List<String>

    // In MessageDao.kt
    @Query("""
    SELECT m.* FROM messages m
    LEFT JOIN message_status s ON m.messageId = s.messageId AND s.userId = :currentUserId
    WHERE m.conversationId = :conversationId 
    AND m.senderId != :currentUserId
    AND (s.readAt IS NULL OR s.messageId IS NULL)
""")
    suspend fun getUnreadMessagesForConversation(conversationId: String, currentUserId: String): List<RoomMessage>
}
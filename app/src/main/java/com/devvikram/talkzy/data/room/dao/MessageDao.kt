package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
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
    suspend fun getMessageByMessageId(messageId: String ): RoomMessage


    @Query("UPDATE messages SET isReceivedBy = :updatedReceivedBy WHERE messageId = :messageId")
    suspend fun updateMessageReceivedByMap(messageId: String, updatedReceivedBy: Map<String, Long>)

    @Query("UPDATE messages SET isReadBy = :updatedReadBy WHERE messageId = :messageId")
    suspend fun updateMessageReadBy(messageId: String, updatedReadBy: Map<String, Long>)


    @Query("UPDATE messages SET lastModifiedAt = :currentTime WHERE messageId = :messageId")
    suspend fun updateLastModifiedAt(messageId: String, currentTime: Long)

    @Query("DELETE FROM messages WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)


}
package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.talkzy.data.room.models.RoomMessage

@Dao
interface MessageDao {

    @Upsert
    suspend fun insertMessage(message: RoomMessage)

    @Upsert
    suspend fun insertMessages(messages: List<RoomMessage>)

    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): RoomMessage?

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId")
    suspend fun getMessagesByConversationId(conversationId: String): List<RoomMessage>


}
package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.talkzy.data.room.models.RoomConversation


@Dao
interface ConversationDao  {

    @Upsert
    suspend fun insertConversation(roomConversation: RoomConversation)

    @Upsert
    suspend fun insertConversations(roomConversations: List<RoomConversation>)

    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    suspend fun getConversationByConversationId(conversationId: String): RoomConversation

    @Query("DELETE FROM conversations WHERE conversationId = :conversationId")
    suspend fun deleteConversation(conversationId: String)


}
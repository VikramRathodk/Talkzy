package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.devvikram.talkzy.data.room.models.RoomConversation


@Dao
interface ConversationDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(roomConversation: RoomConversation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(roomConversations: List<RoomConversation>)


}
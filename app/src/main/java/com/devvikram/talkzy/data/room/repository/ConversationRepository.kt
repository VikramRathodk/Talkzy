package com.devvikram.talkzy.data.room.repository

import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.models.RoomConversation

class ConversationRepository(
    private val conversationDao: ConversationDao
) {

    suspend fun insertConversation(conversation: RoomConversation) {
        conversationDao.insertConversation(conversation)
    }

    suspend fun getConversationByConversationId(conversationId: String): RoomConversation {
        return conversationDao.getConversationByConversationId(conversationId)
    }

    suspend fun deleteConversation(conversationId: String) {
        conversationDao.deleteConversation(conversationId)

    }


}
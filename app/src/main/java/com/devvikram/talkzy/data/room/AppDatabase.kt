package com.devvikram.talkzy.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devvikram.talkzy.config.constants.App
import com.devvikram.talkzy.data.room.converters.Converters
import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomParticipant


@Database(
    entities = [
        RoomConversation::class,
        RoomParticipant::class,
    ],
    version = App.DATABASE_CURRENT_VERSION,
    exportSchema = true
)
@AutoMigration(from = App.DATABASE_LAST_VERSION, to = App.DATABASE_CURRENT_VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun conversationDao(): ConversationDao
}
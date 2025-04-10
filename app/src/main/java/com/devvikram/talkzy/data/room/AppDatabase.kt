package com.devvikram.talkzy.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devvikram.talkzy.config.constants.App
import com.devvikram.talkzy.data.room.converters.Converters
import com.devvikram.talkzy.data.room.dao.ContactsDao
import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.dao.MessageDao
import com.devvikram.talkzy.data.room.dao.MessageStatusDao
import com.devvikram.talkzy.data.room.dao.ParticipantDao
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomMessage
import com.devvikram.talkzy.data.room.models.RoomMessageStatus
import com.devvikram.talkzy.data.room.models.RoomParticipant


@Database(
    entities = [
        RoomContact::class,
        RoomConversation::class,
        RoomParticipant::class,
        RoomMessage::class,
        RoomMessageStatus::class
    ],
    version = App.DATABASE_CURRENT_VERSION,
    exportSchema = true
)
@AutoMigration(from = App.DATABASE_LAST_VERSION, to = App.DATABASE_CURRENT_VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
    abstract fun conversationDao(): ConversationDao
    abstract fun participantDao(): ParticipantDao
    abstract fun messageDao(): MessageDao
    abstract fun messageStatusDao(): MessageStatusDao

}
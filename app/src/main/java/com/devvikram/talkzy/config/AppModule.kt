package com.devvikram.talkzy.config

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.devvikram.talkzy.config.constants.App
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.room.AppDatabase
import com.devvikram.talkzy.data.room.dao.ContactsDao
import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            App.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }


    @Provides
    @Singleton
    fun provideConversationDao(db: AppDatabase) = db.conversationDao()

    @Provides
    @Singleton
    fun provideConversationRepository(conversationDao: ConversationDao) = ConversationRepository(conversationDao)

    @Provides
    @Singleton
    fun provideContactsDao(db: AppDatabase) = db.contactsDao()

    @Provides
    @Singleton
    fun provideContactRepository(contactsDao: ContactsDao) = ContactRepository(contactsDao)

//    @Provides
//    @Singleton
//    fun provideParticipantDao(db: AppDatabase) = db.participantDao()


    @Provides
    @Singleton
    fun provideLoginPreference(@ApplicationContext context: Context): LoginPreference {
        return LoginPreference(context)
    }

}
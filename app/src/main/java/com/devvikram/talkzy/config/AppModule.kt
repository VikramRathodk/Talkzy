package com.devvikram.talkzy.config

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.devvikram.talkzy.config.constants.App
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.firebase.repository.FirebaseContactRepository
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.firebase.repository.FirebaseMessageRepository
import com.devvikram.talkzy.data.room.AppDatabase
import com.devvikram.talkzy.data.room.dao.ContactsDao
import com.devvikram.talkzy.data.room.dao.ConversationDao
import com.devvikram.talkzy.data.room.dao.MessageDao
import com.devvikram.talkzy.data.room.dao.ParticipantDao
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
import com.devvikram.talkzy.data.room.repository.ParticipantRepository
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFireAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
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
    fun provideConversationRepository(
        conversationDao: ConversationDao,
        firebaseConversationRepository: FirebaseConversationRepository,
        @ApplicationContext context: Context,
        participantRepository: ParticipantRepository,
    ) = ConversationRepository(
        conversationDao = conversationDao,
        participantRepository = participantRepository,
        firebaseConversationRepository = firebaseConversationRepository,
    )

    @Provides
    @Singleton
    fun provideContactsDao(db: AppDatabase) = db.contactsDao()

    @Provides
    @Singleton
    fun provideContactRepository(contactsDao: ContactsDao) = ContactRepository(contactsDao)

    @Provides
    @Singleton
    fun provideFirebaseContactRepository(
        firestore: FirebaseFirestore
    ) = FirebaseContactRepository(
        firestore = firestore
    )

    @Provides
    @Singleton
    fun provideFirebaseConversationRepository(
        firestore: FirebaseFirestore
    ) = FirebaseConversationRepository(
        firebaseFirestore = firestore
    )

    @Provides
    @Singleton
    fun provideParticipantDao(db: AppDatabase) = db.participantDao()

    @Provides
    @Singleton
    fun provideParticipantRepository(participantDao: ParticipantDao) =
        ParticipantRepository(participantDao)

    @Provides
    @Singleton
    fun provideMessageDao(db: AppDatabase) = db.messageDao()

    @Provides
    @Singleton
    fun provideMessageRepository(
        messageDao: MessageDao,
        firebaseMessageRepository: FirebaseMessageRepository,
        @ApplicationContext context: Context,
        loginPreference: LoginPreference
    ) = MessageRepository(
        messageDao = messageDao,
        firebaseMessageRepository = firebaseMessageRepository,
        context = context,
        loginPreference = loginPreference
    )


    @Provides
    @Singleton
    fun provideLoginPreference(@ApplicationContext context: Context): LoginPreference {
        return LoginPreference(context)
    }

}
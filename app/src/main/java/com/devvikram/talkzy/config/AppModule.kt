package com.devvikram.talkzy.config

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.devvikram.talkzy.config.constants.App
import com.devvikram.talkzy.data.room.AppDatabase
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
    fun sharedPreference(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences(
        App.USER_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )


}
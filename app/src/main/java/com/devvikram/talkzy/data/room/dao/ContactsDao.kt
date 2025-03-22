package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.devvikram.talkzy.data.room.models.RoomContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Upsert
    suspend fun insertContact(roomContact: RoomContact)

    @Upsert
    suspend fun insertContacts(vararg roomContacts: RoomContact)

    @Upsert
    suspend fun insertAllContacts(roomContacts: List<RoomContact>)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<RoomContact>

    @Query("SELECT * FROM contacts WHERE userId = :contactId")
    suspend fun getContactById(contactId: String): RoomContact?

    @Query("DELETE FROM contacts WHERE userId = :userId")
    suspend fun deleteContactById(userId: String)


    @Query("SELECT * FROM contacts WHERE userId = :userId")
    fun getContactByUserIdWithFlow(userId: String): Flow<RoomContact?>

    @Query("SELECT * FROM contacts")
    fun getAllContactsWithFlow(): Flow<List<RoomContact>>

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()
}
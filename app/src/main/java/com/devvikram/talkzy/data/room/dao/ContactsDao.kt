package com.devvikram.talkzy.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devvikram.talkzy.data.room.models.RoomContact

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(roomContact: RoomContact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(vararg roomContacts: RoomContact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllContacts(roomContacts: List<RoomContact>)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<RoomContact>

    @Query("SELECT * FROM contacts WHERE userId = :contactId")
    suspend fun getContactById(contactId: String): RoomContact?

    @Query("DELETE FROM contacts WHERE userId = :userId")
    suspend fun deleteContactById(userId: String)
}
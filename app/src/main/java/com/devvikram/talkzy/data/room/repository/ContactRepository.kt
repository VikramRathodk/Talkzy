package com.devvikram.talkzy.data.room.repository

import com.devvikram.talkzy.data.room.dao.ContactsDao
import com.devvikram.talkzy.data.room.models.RoomContact
import kotlinx.coroutines.flow.Flow

class ContactRepository(
    private val contactsDao: ContactsDao
)
 {
    suspend fun insertContact(contact: RoomContact) {
        contactsDao.insertContact(contact)
    }

    suspend fun getAllContacts(): List<RoomContact> {
        return contactsDao.getAllContacts()
    }

    suspend fun getContactById(contactId: String): RoomContact? {
        return contactsDao.getContactById(contactId)
    }

     fun getContactByUserIdWithFlow(userId: String): Flow<RoomContact?> {
         return contactsDao.getContactByUserIdWithFlow(userId)
     }

     suspend fun deleteContactById(userId: String) {
         contactsDao.deleteContactById(userId)
     }

     fun getAllContactsWithFlow(): Flow<List<RoomContact>> {
         return contactsDao.getAllContactsWithFlow()
     }



 }
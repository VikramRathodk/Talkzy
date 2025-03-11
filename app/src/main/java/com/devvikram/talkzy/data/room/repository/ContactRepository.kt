package com.devvikram.talkzy.data.room.repository

import com.devvikram.talkzy.data.room.dao.ContactsDao
import com.devvikram.talkzy.data.room.models.RoomContact

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

     suspend fun deleteContactById(userId: String) {
         contactsDao.deleteContactById(userId)
     }


 }
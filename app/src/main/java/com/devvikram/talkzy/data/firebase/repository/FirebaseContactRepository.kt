package com.devvikram.talkzy.data.firebase.repository

import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.FirebaseContact
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseContactRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getContactCollection(): CollectionReference {
        return firestore.collection(FirebaseConstant.FIRESTORE_CONTACTS_COLLECTION)
    }

    fun insertContact(contact: FirebaseContact) {
        getContactCollection().document(contact.userId).set(contact)
    }


    fun updateField(string: String, field: Map<String, Any>) {
        getContactCollection().document(string).update(field)

    }


}
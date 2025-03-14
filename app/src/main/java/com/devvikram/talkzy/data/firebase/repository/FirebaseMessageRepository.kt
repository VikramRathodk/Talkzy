package com.devvikram.talkzy.data.firebase.repository

import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseMessageRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseConversationRepository: FirebaseConversationRepository
) {
    private val logger = Logger.getLogger(FirebaseMessageRepository::class.java.name)

    fun getMessageCollection(conversationId: String, datePartition: String) =
        firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
            .document(conversationId)
            .collection(datePartition)

    fun insertMessage(
        conversationId: String,
        message: ChatMessage,
        datePartition: String
    ) {
        val messageCollectionRef =
            getMessageCollection(conversationId, datePartition)


        messageCollectionRef.add(message).addOnSuccessListener {
            logger.info("Message added with ID: ${it.id}")
        }.addOnFailureListener {
            logger.severe("Error adding message: ${it.message}")  // Handle the error appropriately
        }
    }
}

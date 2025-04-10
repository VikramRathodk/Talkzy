package com.devvikram.talkzy.data.firebase.repository

import android.util.Log
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
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


        messageCollectionRef.document(message.messageId).set(message)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    logger.info("Message inserted successfully: ${message.messageId}")
                } else {
                    logger.severe("Error inserting message: ${it.exception?.message}")
                }
            }
    }
    suspend fun batchUpdateMessagesInFirebase(messages: List<ChatMessage>, datePartition: String) {
        val batch = firestore.batch()

        messages.forEach { message ->
            val docRef = firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
                .document(message.conversationId)
                .collection(datePartition)
                .document(message.messageId)

            batch.set(docRef, message)
        }

        batch.commit()
    }

    suspend fun updateMessageInFirebase(message: ChatMessage,datePartition: String) {
        try {
            firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_COLLECTION)
                .document(message.conversationId)
                .collection(datePartition)
                .document(message.messageId)
                .set(message, SetOptions.merge())
                .await()
            logger.info("Message updated successfully: ${message.messageId}")
        } catch (error: Exception) {
            logger.severe("Error updating message: ${error.message}")
        }
    }
}

package com.devvikram.talkzy.data.firebase.repository

import android.util.Log
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.MessageStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMessageStatusRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){

    fun insertReadStatus(status: MessageStatus) {
        val statusRef = firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_STATUS_COLLECTION)
            .document(status.messageId)
            .collection("message_status")
            .document(status.userId)

        statusRef.set(status)
            .addOnSuccessListener {
                Log.d("FirebaseMessageStatus", "Inserted status for message ${status.messageId}, user ${status.userId}")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseMessageStatus", "Error inserting message status: ${e.message}")
            }
    }


    fun markMessagesAsRead(messageIds: List<String>, userId: String, timestamp: Long) {
        messageIds.forEach { messageId ->
            val statusRef = firestore.collection(FirebaseConstant.FIRESTORE_MESSAGE_STATUS_COLLECTION)
                .document(messageId)
                .collection("message_status")
                .document(userId)

            statusRef.get()
                .addOnSuccessListener { document ->
                    val status = if (document.exists()) {
                        val existing = document.toObject(MessageStatus::class.java)
                        existing?.copy(readAt = timestamp)
                    } else {
                        MessageStatus(
                            messageId = messageId,
                            userId = userId,
                            receivedAt = timestamp,
                            readAt = timestamp
                        )
                    }

                    status?.let {
                        statusRef.set(it)
                            .addOnSuccessListener {
                                Log.d("FirebaseMessageStatus", "Marked message $messageId as read for user $userId")
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirebaseMessageStatus", "Error marking as read: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseMessageStatus", "Error reading status doc: ${e.message}")
                }
        }
    }




}
package com.devvikram.talkzy.data.firebase.repository

import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.Conversation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseConversationRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    fun getConversationCollection() =
        firebaseFirestore.collection(FirebaseConstant.FIRESTORE_CONVERSATION_COLLECTION)

    fun insertConversation(conversation: Conversation) {
        val conversationRef = getConversationCollection().document(conversation.conversationId)

        conversationRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    println("Conversation already exists!")
                } else {
                    conversationRef.set(conversation)
                        .addOnSuccessListener {
                            println("DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            println("Error writing document: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error checking document: ${e.message}")
            }
    }

    fun updateConversationField(
        conversationId: String,
        field: Map<String, Any>
    ) {
        val conversationRef = getConversationCollection().document(conversationId)
        conversationRef.update(field)
            .addOnSuccessListener {
                println("Conversation field updated successfully!")
            }
            .addOnFailureListener { e ->
                println("Error updating conversation field: ${e.message}")
            }
    }


}

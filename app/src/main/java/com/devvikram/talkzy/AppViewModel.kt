package com.devvikram.talkzy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.Conversation
import com.devvikram.talkzy.data.firebase.models.FirebaseContact
import com.devvikram.talkzy.data.firebase.repository.FirebaseContactRepository
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val conversationRepository: ConversationRepository,
    private val contactRepository: ContactRepository,
    private val firebaeContactRepository: FirebaseContactRepository,
    val loginPreference: LoginPreference
) : ViewModel() {


    companion object {
        private const val TAG = "AppViewModel"

    }


    private var conversationListenerRegistration: ListenerRegistration? = null
    private var contactListenerRegistration: ListenerRegistration? = null

    val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    val _isOnBoardingCompleted = MutableLiveData(false)
    val isOnBoardingCompleted: LiveData<Boolean> = _isOnBoardingCompleted

    init {
        _isLoggedIn.value = loginPreference.isLoggedIn()
        _isOnBoardingCompleted.value = loginPreference.isOnBoardingCompleted()
    }


    fun listenToContacts() {
        println("Listening to contacts")
        firebaseFirestore.collection(FirebaseConstant.FIRESTORE_CONTACTS_COLLECTION)
            .addSnapshotListener { snapShots, e ->
                if (e != null) {
                    Log.d(TAG, "listenToContacts:  ${e.message}}")
                    return@addSnapshotListener
                }
                snapShots?.documentChanges?.forEach {
                    val contact = it.document.toObject(FirebaseContact::class.java)

                    Log.d(TAG, "listenToContacts: $contact")

                    viewModelScope.launch {
                        when (it.type) {
                            com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                                println("contact: Document added: ${it.document.data}")
                                contactRepository.insertContact(
                                    ModelMapper.toRoomContact(
                                        contact
                                    )
                                )
                            }

                            com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                println("contact: Document modified: ${it.document.data}")
                                contactRepository.insertContact(
                                    ModelMapper.toRoomContact(
                                        contact
                                    )
                                )
                            }

                            com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                println("contact: Document removed: ${it.document.data}")
                                contactRepository.deleteContactById(contact.userId)
                            }

                            else -> {
                                println("Unknown contact document change type: ${it.type}")
                                // Handle other event types
                            }
                        }
                    }
                }
            }
    }

    fun listenToConversation() {
        conversationListenerRegistration?.remove()
        println("Listening to conversation")
        conversationListenerRegistration =
            firebaseFirestore.collection(FirebaseConstant.FIRESTORE_CONVERSATION_COLLECTION)
                .addSnapshotListener { snapShots, e ->
                    if (e != null) {
                        println("Listen failed: ${e.message}")
                        return@addSnapshotListener
                    }

                    snapShots?.documentChanges?.forEach {
                        println("Document change: ${it.document.data}")

                        val conversation = it.document.toObject(Conversation::class.java)
                        Log.d(TAG, "listenToConversation: $conversation")

                        viewModelScope.launch {
                            when (it.type) {
                                com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                                    println("Document added: ${it.document.data}")
                                    conversationRepository.insertConversation(
                                        ModelMapper.toRoomConversation(
                                            conversation,
                                            loginPreference.getUserId()
                                        )
                                    )
                                    if (conversation.type == "P") {
                                        val loggedInUserId = loginPreference.getUserId()
                                        val receiverId = conversation.participantIds.firstOrNull { it != loggedInUserId }
                                            ?: return@launch
                                        val existingContact =
                                            contactRepository.getContactById(receiverId)
                                        if (existingContact != null) {
                                            contactRepository.insertContact(
                                                existingContact.copy(
                                                    conversationId = conversation.conversationId
                                                )
                                            )
                                            firebaeContactRepository.updateField(
                                                existingContact.userId, mapOf(
                                                    "conversationId" to conversation.conversationId,
                                                    "lastModifiedAt" to System.currentTimeMillis()
                                                )
                                            )

                                        }
                                    }
                                }

                                com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                    println("Document modified: ${it.document.data}")

                                    conversationRepository.insertConversation(
                                        ModelMapper.toRoomConversation(
                                            conversation,
                                            loginPreference.getUserId()
                                        )
                                    )
                                }

                                com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                    println("Document removed: ${it.document.data}")
                                    conversationRepository.deleteConversation(conversation.conversationId)
                                }

                                else -> {
                                    println("Unknown document change type: ${it.type}")
                                }
                            }
                        }


                    }

                }
    }

    override fun onCleared() {
        super.onCleared()
        contactListenerRegistration?.remove()
        conversationListenerRegistration?.remove()
    }
}
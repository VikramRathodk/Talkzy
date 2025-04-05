package com.devvikram.talkzy.ui.screens.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.config.enums.RoleType
import com.devvikram.talkzy.data.firebase.config.FirebaseConstant
import com.devvikram.talkzy.data.firebase.models.Participant
import com.devvikram.talkzy.data.firebase.repository.FirebaseConversationRepository
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.models.RoomConversation
import com.devvikram.talkzy.data.room.models.RoomParticipant
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.ParticipantRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    val loginPreference: LoginPreference,
    private val firebaseFireStore: FirebaseFirestore,
    private val participantRepository: ParticipantRepository,
    private val conversationRepository: ConversationRepository,
    private val firebaseConversationRepository: FirebaseConversationRepository
) : ViewModel() {
    private val _isCreateGroupDialogOpened = MutableStateFlow(false)
    val isCreateGroupDialogOpened = _isCreateGroupDialogOpened.asStateFlow()

    private val _contactsList = MutableStateFlow<List<RoomContact>>(emptyList());
    val contactList = _contactsList.asStateFlow()

    private val _selectedContacts = MutableStateFlow<List<RoomContact>>(emptyList());
    val selectedContacts = _selectedContacts.asStateFlow()

    init {
        viewModelScope.launch {
            contactRepository.getAllContactsWithFlow().collect {
                _contactsList.value = it.filter {
                    it.userId != loginPreference.getUserId()
                }
            }
        }
    }

    fun updateIsCreateGroupDialogOpened(value: Boolean) {
        _isCreateGroupDialogOpened.value = value
    }

    fun toggleContactSelection(contact: RoomContact) {
        val currentSelection = _selectedContacts.value
        if (currentSelection.contains(contact)) {
            _selectedContacts.value = currentSelection - contact
        } else {
            _selectedContacts.value = currentSelection + contact
        }
    }

    fun createAGroup(
        groupName: String,
        groupDescription: String,
        contacts: List<RoomContact>
    ) {
        viewModelScope.launch {
            val conversationId =
                firebaseFireStore.collection(FirebaseConstant.FIRESTORE_CONVERSATION_COLLECTION)
                    .document().id
            println("createAGroup: $groupName, $groupDescription, $contacts")
            // step one create a participant
            val roomParticipants =
                getRoomParticipants(conversationId = conversationId, contacts = contacts)
            val fParticipants = getFirebaseParticipants(conversationId = conversationId, contacts)
            val conversation = RoomConversation(
                conversationId = conversationId,
                userId = "",
                type = "G",
                description = groupDescription,
                name = groupName,
                createdBy = loginPreference.getUserId(),
                createdAt = System.currentTimeMillis(),
                lastModifiedAt = System.currentTimeMillis(),
                participantIds = contacts.map { it.userId } + loginPreference.getUserId(),
            )
            participantRepository.insertAllParticipants(roomParticipants)
            conversationRepository.insertConversation(conversation)
            firebaseConversationRepository.insertConversation(
                ModelMapper.toFirebaseConversation(
                    conversation,
                    fParticipants
                )
            )
        }
    }

    private fun getFirebaseParticipants(
        conversationId: String,
        contacts: List<RoomContact>
    ): List<Participant> {
        val currentUserId = loginPreference.getUserId()
        return contacts.map {
            Participant(
                userId = it.userId,
                role = RoleType.MEMBER.name
            )
        } + Participant(
            userId = currentUserId,
            role = RoleType.CREATOR.name
        )
    }

    private fun getRoomParticipants(
        conversationId: String,
        contacts: List<RoomContact>
    ): List<RoomParticipant> {
        val currentUserId = loginPreference.getUserId()
        return contacts.map {
            RoomParticipant(
                conversationId = conversationId,
                userId = it.userId,
                localParticipantId = 0,
                role = RoleType.MEMBER.name
            )
        } + RoomParticipant(
            conversationId = conversationId,
            userId = currentUserId,
            localParticipantId = 0,
            role = RoleType.CREATOR.name
        )
    }

}
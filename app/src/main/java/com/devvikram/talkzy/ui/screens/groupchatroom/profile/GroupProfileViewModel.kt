package com.devvikram.talkzy.ui.screens.groupchatroom.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.data.room.models.ConversationWithContacts
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupProfileViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {


    private val _groupInformation = MutableStateFlow<ConversationWithContacts?>(null)
    val groupInformation: StateFlow<ConversationWithContacts?> = _groupInformation.asStateFlow()

    private val _conversationId = MutableStateFlow("")
    val conversationId = _conversationId.asStateFlow()

    fun setConversationId (conversationId: String){
        _conversationId.update { conversationId }
    }

    init {
        viewModelScope.launch {
            _conversationId.collectLatest { id ->
                if (id.isNotBlank()) {
                    launch { observeConversationInfo(id) }
                }
            }
        }
    }

    private suspend fun observeConversationInfo(conversationId: String) {
        conversationRepository.getConversationByConversationIdFlow(_conversationId.value)
            .collectLatest {
                _groupInformation.value = it
            }
    }
}
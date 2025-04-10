package com.devvikram.talkzy.ui.screens.conversations

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.room.models.ConversationWithContacts
import com.devvikram.talkzy.data.room.models.LastMessage
import com.devvikram.talkzy.data.room.repository.ConversationRepository
import com.devvikram.talkzy.data.room.repository.MessageRepository
import com.devvikram.talkzy.ui.models.FilterOption
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ConversationViewmodel @Inject constructor(
    val loginPreference: LoginPreference,
    @ApplicationContext val context: Context,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _filterOptionList = MutableStateFlow(
        listOf(
            FilterOption("All", true, icon = { }),
            FilterOption("Personal", false, icon = { }),
            FilterOption("Group", false, icon = { }),
            FilterOption("Unread", false, icon = { }),
            FilterOption("Archived", false, icon = { }),
            FilterOption("Muted", false, icon = { }),
            FilterOption("Starred", false, icon = { })
        )
    )

    val filterOptionList: StateFlow<List<FilterOption>> = _filterOptionList



    fun getLastMessageFlow(conversationId: String): Flow<LastMessage> {
        return messageRepository.getLastMessageFlow(conversationId).map {
            ModelMapper.mapToLastMessage(it)
        }
    }


    fun setSelection(selectedOption: FilterOption) {
        _filterOptionList.value = _filterOptionList.value.map {
            it.copy(isSelected = it.name == selectedOption.name)
        }
    }


    val conversations: Flow<List<ConversationWithContacts>> =
        conversationRepository.getAllConversationWithContactFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


}
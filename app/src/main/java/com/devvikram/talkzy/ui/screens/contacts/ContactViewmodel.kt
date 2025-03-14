package com.devvikram.talkzy.ui.screens.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ContactViewmodel @Inject constructor(
    @ApplicationContext val context :Context,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    val contacts  : Flow<List<RoomContact>> = contactRepository.getAllContactsWithFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )



}
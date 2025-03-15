package com.devvikram.talkzy.ui.screens.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewmodel @Inject constructor(
    @ApplicationContext val context: Context,
    private val contactRepository: ContactRepository,
    val loginPreference: LoginPreference
) : ViewModel() {

    private val _contactsList = MutableStateFlow<List<RoomContact>>(emptyList());
    val contactList = _contactsList.asStateFlow()

    init {
        viewModelScope.launch {
            contactRepository.getAllContactsWithFlow().collect {
                _contactsList.value = it.filter {
                    it.userId != loginPreference.getUserId()
                }
            }
        }
    }


}
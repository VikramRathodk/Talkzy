package com.devvikram.talkzy.ui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewmodel  @Inject constructor(
    private val contactRepository: ContactRepository,
    val loginPreference: LoginPreference,
):ViewModel(){


    // isHomeToolbar visible flow variable
    private val _isHomeToolbarVisible = MutableStateFlow<Boolean>(true)
    val isHomeToolbarVisibleFlow : StateFlow<Boolean> = _isHomeToolbarVisible.asStateFlow()

    val loggedUser : Flow<RoomContact?> = contactRepository.getContactByUserIdWithFlow(loginPreference.getUserId()).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )


    fun updateHomeToolbarState(value: Boolean) {
        _isHomeToolbarVisible.value = value
    }

}
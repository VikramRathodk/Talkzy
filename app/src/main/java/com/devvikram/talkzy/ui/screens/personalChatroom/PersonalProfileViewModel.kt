package com.devvikram.talkzy.ui.screens.personalChatroom

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonalProfileViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val contactRepository: ContactRepository,

) : ViewModel() {

   private val _userId = MutableStateFlow("")

    private val _userProfile = MutableStateFlow<RoomContact?>(null)
    val userProfile: StateFlow<RoomContact?> = _userProfile.asStateFlow()

    fun setUserId(userId: String) {
        _userId.value = userId
        viewModelScope.launch {

            _userId.collectLatest {
                getUserDetails(it)
            }

        }
    }

    suspend fun getUserDetails(userId: String) {
        contactRepository.getContactByUserIdWithFlow(userId).collectLatest {
            _userProfile.value = it
        }
    }

}
package com.devvikram.talkzy.ui.screens.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import com.devvikram.talkzy.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ContactViewmodel @Inject constructor(
    @ApplicationContext val context :Context,
    private val contactRepository: ContactRepository,
) : ViewModel() {
}
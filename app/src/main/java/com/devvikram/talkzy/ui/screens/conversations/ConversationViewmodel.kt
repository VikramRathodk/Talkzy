package com.devvikram.talkzy.ui.screens.conversations

import android.content.Context
import androidx.lifecycle.ViewModel
import com.devvikram.talkzy.config.constants.LoginPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class ConversationViewmodel @Inject constructor(
    val loginPreference: LoginPreference,
    @ApplicationContext val context: Context
) : ViewModel() {

}
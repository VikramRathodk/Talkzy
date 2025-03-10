package com.devvikram.talkzy

import androidx.lifecycle.ViewModel
import com.devvikram.talkzy.data.firebase.FirebaseConstant
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
) :  ViewModel() {

    fun listenToConversation() {
        println("Listening to conversation")
    }

    init {
        println("AppViewModel initialized")
    }

}
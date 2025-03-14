package com.devvikram.talkzy.ui.screens.groupchatroom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel

@Composable
fun GroupChatroomScreen(
    appViewmodel: AppViewModel,
    appLevelNavController: NavHostController,

    ) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = "Group Chatroom Screen"
            )
        }
    }

}
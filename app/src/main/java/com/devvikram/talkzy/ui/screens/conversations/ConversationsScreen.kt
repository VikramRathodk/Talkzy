package com.devvikram.talkzy.ui.screens.conversations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.screens.HomeViewmodel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    homeNavigationController: NavHostController,
    homeViewmodel: HomeViewmodel,
    conversationViewmodel: ConversationViewmodel,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Conversations Screen",

            )

    }
}
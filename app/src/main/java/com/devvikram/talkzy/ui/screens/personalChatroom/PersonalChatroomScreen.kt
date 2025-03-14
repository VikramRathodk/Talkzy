package com.devvikram.talkzy.ui.screens.personalChatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalChatroomScreen(
    appViewmodel: AppViewModel,
    appLevelNavController: NavHostController,
    conversationId: String,
    personalChatRoomViewmodel: PersonalChatRoomViewmodel = hiltViewModel(),
    receiverId : String
) {
    Log.d(TAG, "Opening PersonalChatroomScreen with conversationId: $conversationId")

    // Runs only when `conversationId` changes
    LaunchedEffect(conversationId) {
        personalChatRoomViewmodel.setConversationId(conversationId)
        personalChatRoomViewmodel.getReceiverInfo(receiverId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chatroom") },
                navigationIcon = {
                    IconButton(onClick = { appLevelNavController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages List
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(15) { message ->
                    ChatMessageItem(message)
                }
            }

            // Message Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var messageText by remember { mutableStateOf("") }

                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") }
                )

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            personalChatRoomViewmodel.sendMessage(messageText)
                            messageText = ""
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message")
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(x0: Int) {
    Text(
        text = "Message $x0",
        modifier = Modifier.padding(16.dp)
    )
}

package com.devvikram.talkzy.ui.screens.personalChatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.ReceiverImageMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.ReceiverTextMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.SenderImageMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.SenderTextMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.TypingIndicatorBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalChatroomScreen(
    appViewmodel: AppViewModel,
    appLevelNavController: NavHostController,
    conversationId: String,
    personalChatRoomViewmodel: PersonalChatRoomViewmodel = hiltViewModel(),
    receiverId: String
) {
    Log.d(TAG, "Opening PersonalChatroomScreen with conversationId: $conversationId")

    // Runs only when `conversationId` changes
    LaunchedEffect(conversationId) {
        personalChatRoomViewmodel.setConversationId(conversationId)
        personalChatRoomViewmodel.getReceiverInfo(receiverId)
    }
    val chatMessageList = personalChatRoomViewmodel.chatMessageList.collectAsStateWithLifecycle()

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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                reverseLayout = true
            ) {
                items(chatMessageList.value) { message ->
                    when (message) {
                        is PersonalChatMessageItem.SenderTextMessageItem -> SenderTextMessageBubble(message)
                        is PersonalChatMessageItem.ReceiverTextMessageItem -> ReceiverTextMessageBubble(message)
                        is PersonalChatMessageItem.SenderImageMessageItem -> SenderImageMessageBubble(message)
                        is PersonalChatMessageItem.ReceiverImageMessageItem -> ReceiverImageMessageBubble(message)
                        is PersonalChatMessageItem.TypingIndicator -> TypingIndicatorBubble()
                    }
                }
            }


            // Message Input
            OutlinedMessageInput { message ->
                personalChatRoomViewmodel.sendMessage(message)
            }

        }
    }
}
@Composable
fun OutlinedMessageInput(
    onMessageSend: (String) -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF4CAF50),
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (messageText.isNotBlank()) {
                        onMessageSend(messageText)
                        messageText = ""
                    }
                }
            )
        )

        AnimatedVisibility(visible = messageText.isNotBlank()) {
            IconButton(
                onClick = {
                    onMessageSend(messageText)
                    messageText = ""
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF4CAF50), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}





package com.devvikram.talkzy.ui.screens.personalChatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.R
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.reuseables.MessageInput
import com.devvikram.talkzy.ui.reuseables.ProfileImage
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
    Log.d(TAG, "PersonalChatroomScreen: ReceiverId : $receiverId")

    val isDarkMode = isSystemInDarkTheme()
    val chatMessageList = personalChatRoomViewmodel.chatMessageList.collectAsStateWithLifecycle()
    val receiverProfile =
        personalChatRoomViewmodel.receiverUserProfile.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val backGroupResource = if (isDarkMode) {
        R.drawable.chat_bg_dark_blue
    } else {
        R.drawable.chat_bg_white_light_blue_pattern
    }
    // Runs only when `conversationId` changes
    LaunchedEffect(conversationId) {
        personalChatRoomViewmodel.setConversationId(conversationId)
        personalChatRoomViewmodel.getReceiverInfo(receiverId)
    }
    LaunchedEffect(chatMessageList.value.size) {
        if (chatMessageList.value.isNotEmpty()) {
            listState.animateScrollToItem(chatMessageList.value.lastIndex)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            ChatTopBar(
                receiverProfile = receiverProfile.value,
                onBackClick = { appLevelNavController.popBackStack() },
                onCallClick = { },
                onVideoCallClick = { },
                onMoreOptionsClick = { },
                onClick = {
                    appLevelNavController.navigate(
                        HomeNavigationDestination.PersonalProfileDest.createRoute(
                            conversationId,
                            receiverId
                        )
                    )
                }
            )
        },
        bottomBar = {
            MessageInput(
                modifier = Modifier.navigationBarsPadding(),
                onMessageSend = { message -> personalChatRoomViewmodel.sendMessage(message) },
                onCameraClick = {},
                onAttachmentClick = {}
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
//                .paint(
//                    painterResource(id = backGroupResource),
//                    contentScale = ContentScale.Crop,
//                    alignment = Alignment.TopCenter
//                )
                .padding(paddingValues)
        ) {
            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f),
                reverseLayout = false
            ) {
                items(chatMessageList.value) { message ->
                    when (message) {
                        is PersonalChatMessageItem.SenderTextMessageItem -> SenderTextMessageBubble(
                            message
                        )

                        is PersonalChatMessageItem.ReceiverTextMessageItem -> ReceiverTextMessageBubble(
                            message
                        )

                        is PersonalChatMessageItem.SenderImageMessageItem -> SenderImageMessageBubble(
                            message
                        )

                        is PersonalChatMessageItem.ReceiverImageMessageItem -> ReceiverImageMessageBubble(
                            message
                        )

                        is PersonalChatMessageItem.TypingIndicator -> TypingIndicatorBubble()
                    }
                }
            }

        }
    }
}







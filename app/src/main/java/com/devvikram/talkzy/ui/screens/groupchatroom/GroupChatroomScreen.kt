package com.devvikram.talkzy.ui.screens.groupchatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.R
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.reuseables.MessageInput
import com.devvikram.talkzy.ui.screens.groupchatroom.itemviews.GroupReceiverImageMessageBubble
import com.devvikram.talkzy.ui.screens.groupchatroom.itemviews.GroupReceiverTextMessageBubble
import com.devvikram.talkzy.ui.screens.groupchatroom.itemviews.GroupSenderImageMessageBubble
import com.devvikram.talkzy.ui.screens.groupchatroom.itemviews.GroupSenderTextMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.ChatTopBar
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.ReceiverImageMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.ReceiverTextMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.SenderImageMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.SenderTextMessageBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.itemviews.TypingIndicatorBubble
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem

@Composable
fun GroupChatroomScreen(
    appViewmodel: AppViewModel,
    appLevelNavController: NavHostController,
    conversationId: String,
    groupChatViewmodel: GroupChatViewmodel = hiltViewModel(),
) {
    Log.d(TAG, "Opening PersonalChatroomScreen with conversationId: $conversationId")

    val isDarkMode = isSystemInDarkTheme()
    val chatMessageList = groupChatViewmodel.chatMessageList.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val backGroupResource = if (isDarkMode) {
        R.drawable.chat_bg_dark_blue
    } else {
        R.drawable.chat_bg_white_light_blue_pattern
    }
    // Runs only when `conversationId` changes
    LaunchedEffect(conversationId) {
        groupChatViewmodel.setConversationId(conversationId)
        appViewmodel.setCurrentConversationId(conversationId)
    }
    LaunchedEffect(chatMessageList.value.size) {
        if (chatMessageList.value.isNotEmpty()) {
            listState.animateScrollToItem(chatMessageList.value.lastIndex)
        }
    }
    DisposableEffect(
        conversationId
    ) {
        onDispose {
            appViewmodel.setCurrentConversationId("")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            GroupChatTopBar(
                groupChatViewmodel = groupChatViewmodel,
                onBackPressed = {
                    appLevelNavController.popBackStack()
                },
                onClick = {
                    appLevelNavController.navigate(
                        HomeNavigationDestination.GroupProfile(
                            conversationId = conversationId
                        ).route
                    )
                }
            )
        },
        bottomBar = {
            MessageInput(
                modifier = Modifier.navigationBarsPadding(),
                onMessageSend = { message -> groupChatViewmodel.sendMessage(message) },
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
                        is GroupChatMessageItem.SenderTextMessageItem -> GroupSenderTextMessageBubble(
                            message
                        )

                        is GroupChatMessageItem.ReceiverTextMessageItem -> GroupReceiverTextMessageBubble(
                            message
                        )

                        is GroupChatMessageItem.SenderImageMessageItem -> GroupSenderImageMessageBubble(
                            message
                        )

                        is GroupChatMessageItem.ReceiverImageMessageItem -> GroupReceiverImageMessageBubble(
                            message
                        )

                        is GroupChatMessageItem.TypingIndicator -> TypingIndicatorBubble()
                    }
                }
            }

        }
    }

}
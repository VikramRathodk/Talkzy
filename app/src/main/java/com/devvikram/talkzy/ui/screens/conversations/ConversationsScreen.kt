package com.devvikram.talkzy.ui.screens.conversations

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.talkzy.data.room.models.LastMessage
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.screens.HomeViewmodel
import com.devvikram.talkzy.ui.screens.conversations.itemviews.GroupConversationItemCard
import com.devvikram.talkzy.ui.screens.conversations.itemviews.PersonalConversationItemCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    homeNavigationController: NavHostController,
    homeViewmodel: HomeViewmodel,
    conversationViewmodel: ConversationViewmodel,
    appLevelNavController: NavHostController
) {
    val conversations = conversationViewmodel.conversations.collectAsState(emptyList())
    val loggedUser = homeViewmodel.loggedUser.collectAsState(initial = null).value
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // conversations iist
        items(conversations.value.size) { item ->
            val conversation = conversations.value[item].conversation
            val contacts = conversations.value[item].participants

            val conversationItem = when (conversation.type) {
                "P" -> {
                    ConversationItem.PersonalConversation(
                        conversationId = conversation.conversationId,
                        userId = contacts?.userId ?: "",
                        name = conversation.name.toString(),
                        unreadMessageCount = 0,
                        selected = false,
                        timeStamp = conversation.createdAt,
                        lastMessage = LastMessage(
                            text = "Nice I'll do that.",
                            type = "IMAGE",
                            senderId = conversation.userId,
                            timestamp = conversation.createdAt,
                            mediaUrl = "",
                        )
                    )
                }

                "G" -> {
                    ConversationItem.GroupConversation(
                        conversationId = conversation.conversationId,
                        groupTitle = conversation.name.toString(),
                        unreadMessageCount = 0,
                        participants = conversation.participantIds,
                        selected = false,
                        timeStamp = conversation.createdAt,
                        lastMessage = LastMessage(
                            text = "Hello, How are you?",
                            type = "VIDEO",
                            senderId = conversation.userId,
                            timestamp = conversation.createdAt,
                            mediaUrl = "",
                        )
                    )
                }

                else -> {}
            }
            when (conversationItem) {
                is ConversationItem.PersonalConversation -> {
                    PersonalConversationItemCard(
                        conversationItem = conversationItem,
                        modifier = Modifier,
                        homeNavigationController = homeNavigationController,
                        onClick = {
                            Toast.makeText(context, "Personal Clicked", Toast.LENGTH_SHORT).show()
                            appLevelNavController.navigate(
                                HomeNavigationDestination.PersonalChatroomDest.createRoute(
                                    conversationItem.conversationId,
                                    conversationItem.userId
                                )
                            )

                        }
                    )
                }

                is ConversationItem.GroupConversation -> {
                    GroupConversationItemCard(
                        conversationItem = conversationItem,
                        modifier = Modifier,
                        homeNavigationController = homeNavigationController,
                        onClick = {
                            Toast.makeText(context, "Group Clicked", Toast.LENGTH_SHORT).show()
                            appLevelNavController.navigate(HomeNavigationDestination.GroupChatroomDest.route)
                        }
                    )
                }

                else -> {}
            }

        }

    }

}



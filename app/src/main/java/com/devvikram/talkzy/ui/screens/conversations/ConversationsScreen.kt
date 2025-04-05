package com.devvikram.talkzy.ui.screens.conversations

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue

import androidx.navigation.NavHostController
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.screens.HomeViewmodel
import com.devvikram.talkzy.ui.screens.conversations.filters.FilterOptionsBar
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
        // filter options
        item {
            FilterOptionsBar(conversationViewmodel = conversationViewmodel)
        }

        // conversations iist
        items(conversations.value) { item ->
            val conversation = item.conversation
            val contact = item.contact
            Log.d(TAG, "ConversationsScreen: contact $contact")
            val lastMessage by conversationViewmodel.getLastMessageFlow(conversation.conversationId ).collectAsState(initial = null)

            val unReadMessageCount by conversationViewmodel.getUnreadMessageCountFlow(conversation.conversationId).collectAsState(initial = 0)

            val conversationItem = when (conversation.type) {
                "P" -> {
                    ConversationItem.PersonalConversation(
                        conversationId = conversation.conversationId,
                        userId = contact?.userId ?: "",
                        name = contact?.name.toString(),
                        unreadMessageCount = unReadMessageCount,
                        selected = false,
                        timeStamp = conversation.createdAt,
                        lastMessage = lastMessage,
                    )
                }

                "G" -> {
                    ConversationItem.GroupConversation(
                        conversationId = conversation.conversationId,
                        groupTitle = conversation.name.toString(),
                        unreadMessageCount = unReadMessageCount,
                        participants = conversation.participantIds,
                        selected = false,
                        timeStamp = conversation.createdAt,
                        lastMessage = lastMessage
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
                            appLevelNavController.navigate(
                                HomeNavigationDestination.GroupChatroomDest.createRoute(
                                    conversationItem.conversationId,
                                )
                            )
                        }
                    )
                }

                else -> {}
            }

        }

    }

}



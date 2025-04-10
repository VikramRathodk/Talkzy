package com.devvikram.talkzy.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class HomeNavigationDestination(val route: String) {

    @Serializable
    data object HomeDest : HomeNavigationDestination("home")

    @Serializable
    data object ConversationDest : HomeNavigationDestination("conversation")

    @Serializable
    data object ContactsDest : HomeNavigationDestination("contacts")

    @Serializable
    data object Call : HomeNavigationDestination("call")

    @Serializable
    data object Settings : HomeNavigationDestination("settings")


    @Serializable
    data class PersonalChatroomDest(
        val conversationId: String,
        val receiverId: String
    ) : HomeNavigationDestination("personalChatroom/{conversationId}/{receiverId}") {
        companion object {
            fun createRoute(conversationId: String, receiverId: String) =
                "personalChatroom/$conversationId/$receiverId"
        }
    }

    // personal profile
    @Serializable
    data class PersonalProfileDest(
        val conversationId: String,
        val userId: String
    ) : HomeNavigationDestination("personalProfile/{conversationId}/{receiverId}") {
        companion object {
            fun createRoute(conversationId: String, receiverId: String) =
                "personalProfile/$conversationId/$receiverId"
        }
    }
    @Serializable
    data class GroupProfile(
        val conversationId: String,
    ) : HomeNavigationDestination("groupProfile/{conversationId}") {
        companion object {
            fun createRoute(conversationId: String) =
                "groupProfile/$conversationId"
        }
    }

    @Serializable
    data class GroupChatroomDest(
        val conversationId: String,
    ) : HomeNavigationDestination("groupChatroom/{conversationId}"){
        companion object {
            fun createRoute(conversationId: String) =
                "groupChatroom/$conversationId"
        }
    }

    @Serializable
    data object CreateGroupDest : HomeNavigationDestination("groupCreate")

}

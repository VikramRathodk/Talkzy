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
}

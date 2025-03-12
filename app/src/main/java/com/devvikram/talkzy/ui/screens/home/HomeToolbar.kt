package com.devvikram.talkzy.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeToolbar(
    appViewModel: AppViewModel,
    homeNavigationController: NavHostController,
    currentDestination: NavDestination?
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val title = when (currentDestination?.route) {
        HomeNavigationDestination.ConversationDest.route -> "Chats"
        HomeNavigationDestination.ContactsDest.route -> "Contacts"
        HomeNavigationDestination.Call.route -> "Calls"
        HomeNavigationDestination.Settings.route -> "Settings"
        else -> "Talkzy"
    }

    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(
                onClick = { showLogoutDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Logout"
                )
            }
        }
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        appViewModel._isLoggedIn.value = false
                        appViewModel.loginPreference.setLoggedIn(false)
                        appViewModel.loginPreference.setOnBoardingCompleted(true)
                        appViewModel._isOnBoardingCompleted.value = true
                        showLogoutDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

package com.devvikram.talkzy.ui.screens.home




import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call

import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun HomeBottomBar(navController: NavController, currentDestination: NavDestination?) {
    val bottomNavItems = listOf(
        BottomNavItem("Chats", Icons.Filled.Email, HomeNavigationDestination.ConversationDest.route),
        BottomNavItem("Call", Icons.Filled.Call, HomeNavigationDestination.Call.route),
        BottomNavItem("Settings", Icons.Filled.Settings, HomeNavigationDestination.Settings.route)
    )

    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

package com.devvikram.talkzy.ui.screens.home


//
//data class BottomNavItem(
//    val label: String,
//    val icon: ImageVector,
//    val route: String
//)
//
//@Composable
//fun HomeBottomBar(navController: NavController, currentDestination: NavDestination?) {
//    val bottomNavItems = listOf(
//        BottomNavItem(
//            "Chats",
//            Icons.Filled.Email,
//            HomeNavigationDestination.ConversationDest.route
//        ),
//        BottomNavItem("Call", Icons.Filled.Call, HomeNavigationDestination.Call.route),
//        BottomNavItem("Settings", Icons.Filled.Settings, HomeNavigationDestination.Settings.route),
//    )
//
//    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.onPrimary,
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
//        ) {
//        bottomNavItems.forEach { item ->
//            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
//
//            NavigationBarItem(
//                icon = { Icon(item.icon, contentDescription = item.label) },
//                label = { Text(item.label) },
//                selected = isSelected,
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
//                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
//                    selectedTextColor = MaterialTheme.colorScheme.primary,
//                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
//                    indicatorColor = MaterialTheme.colorScheme.primary.copy(
//                        alpha = 0.8f
//                    ),
//                    disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
//                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
//                ),
//                onClick = {
//                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.startDestinationId) { saveState = true }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import com.devvikram.talkzy.R

data class BottomNavItem(
    val label: String,
    val iconRes: Int,
    val route: String
)

@Composable
fun HomeBottomBar(navController: NavController, currentDestination: NavDestination?) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Chats",
            iconRes = R.drawable.baseline_chat_24,
            route = HomeNavigationDestination.ConversationDest.route
        ),
        BottomNavItem(
            label = "Call",
            iconRes = R.drawable.baseline_call_24,
            route = HomeNavigationDestination.Call.route
        ),
        BottomNavItem(
            label = "Profile",
            iconRes = R.drawable.baseline_person_24,
            route = HomeNavigationDestination.PersonalProfileDest.createRoute("0", "0")
        ),
        BottomNavItem(
            label = "Settings",
            iconRes = R.drawable.baseline_settings_24,
            route = HomeNavigationDestination.Settings.route
        ),
    )


    val selectedIndex = bottomNavItems.indexOfFirst {
        currentDestination?.hierarchy?.any { dest -> dest.route == it.route } == true
    }.coerceAtLeast(0)

    AnimatedNavigationBar(
        modifier = Modifier
            .fillMaxWidth()
        ,
        selectedIndex = selectedIndex,
        barColor = MaterialTheme.colorScheme.secondary,
        ballColor = MaterialTheme.colorScheme.primary,
    ) {
        bottomNavItems.forEachIndexed { index, item ->
            DropletButton(
                 modifier = Modifier.padding(horizontal = 8.dp, vertical = 32.dp),
                icon = item.iconRes,
                contentDescription = item.label,
                isSelected = index == selectedIndex,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                size = 24.dp,
                iconColor = MaterialTheme.colorScheme.onPrimary,
                dropletColor = MaterialTheme.colorScheme.primary,
                animationSpec = tween(300)
            )
        }
    }
}


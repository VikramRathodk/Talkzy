package com.devvikram.talkzy.ui.screens.home

import android.R.attr.offset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.R
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeToolbar(
    appViewModel: AppViewModel,
    homeNavigationController: NavHostController,
    currentDestination: NavDestination?,
    loggedUser: RoomContact?,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val title = when (currentDestination?.route) {
        HomeNavigationDestination.ConversationDest.route -> "Chats"
        HomeNavigationDestination.ContactsDest.route -> "Contacts"
        HomeNavigationDestination.Call.route -> "Calls"
        HomeNavigationDestination.Settings.route -> "Settings"
        else -> "Talkzy"
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hello_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )


    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            if (currentDestination?.route == HomeNavigationDestination.ConversationDest.route) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .size(60.dp)
                    )
                    Text(
                        text = "${loggedUser?.name}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },

        actions = {
            IconButton(
                onClick = { showLogoutDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                appViewModel._isLoggedIn.value = false
                appViewModel.loginPreference.setLoggedIn(false)
                appViewModel.loginPreference.setOnBoardingCompleted(true)
                appViewModel._isOnBoardingCompleted.value = true
                appViewModel.clearLocalDatabase()
                showLogoutDialog = false
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }
}

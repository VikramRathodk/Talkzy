package com.devvikram.talkzy.ui.screens.personalChatroom

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel


import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.devvikram.talkzy.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.constraintlayout.compose.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devvikram.talkzy.data.room.models.RoomContact


@Composable
fun PersonalProfileScreen(
    conversationId: String,
    appViewmodel: AppViewModel,
    appLevelNavController: NavHostController,
    receiverId: String,
    personalProfileViewModel: PersonalProfileViewModel = hiltViewModel()
) {
    Log.d(TAG, "PersonalProfileScreen: $conversationId receiverId $receiverId")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)

    LaunchedEffect(receiverId) {
        personalProfileViewModel.setUserId(receiverId)
    }
    // Fetch user details from ViewModel or local database
    val user by personalProfileViewModel.userProfile.collectAsStateWithLifecycle()

    // Animate visibility of profile content
    var isProfileLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(user) {
        if (user != null) {
            isProfileLoaded = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Animated background
        AnimatedBackground()

        // Profile Content
        if (isProfileLoaded && user != null) {
            AnimatedProfileContent(user!!)
        } else {
            // Loading indicator
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
    )
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
private fun AnimatedProfileContent(user: RoomContact) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Image(
            painter = painterResource(id = R.drawable.person_icon),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
            ,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Online Status
        if (user.isOnline) {
            Text(
                text = "Online",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green,
                modifier = Modifier
            )
        } else {
            Text(
                text = "Last seen: ${formatLastSeen(user.lastSeen)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Bio
        Text(
            text = user.status,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)

        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* Handle message action */ },
                modifier = Modifier
            ) {
                Text("Message")
            }
            Button(
                onClick = { /* Handle call action */ },
                modifier = Modifier
            ) {
                Text("Call")
            }
        }
    }
}

private fun formatLastSeen(lastSeen: Long): String {
    return "2 minutes ago"
}



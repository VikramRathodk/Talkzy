package com.devvikram.talkzy.ui.screens.personalChatroom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.ui.reuseables.ProfileImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    receiverProfile: RoomContact?,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
    onClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(
                        onClick = { onClick() }
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .padding(2.dp)
                ) {
                    // Animated border for online users
                    if (receiverProfile?.isOnline == true) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        )
                    }

                    ProfileImage(
                        imagePath = receiverProfile?.localProfilePicturePath,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = if (receiverProfile?.isOnline == true)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                else
                                    Color.LightGray,
                                shape = CircleShape
                            )
                            .background(
                                color = Color.LightGray.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                    )

                    // Online indicator dot
                    if (receiverProfile?.isOnline == true) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .border(1.dp, Color.White, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Username & Status with improved typography
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = receiverProfile?.name ?: "Unknown",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Show online status or last seen with better visual cues
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = receiverProfile?.isOnline == true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Text(
                            text = when {
                                receiverProfile?.isOnline == true -> "Online"
                                receiverProfile?.lastSeen != null -> AppUtils.getTimeFromTimeStamp(receiverProfile.lastSeen)
                                else -> "Offline"
                            },
                            color = when {
                                receiverProfile?.isOnline == true -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            // Call button with ripple effect
            IconButton(
                onClick = onCallClick,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Call",
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            // More Options with improved styling
            IconButton(
                onClick = onMoreOptionsClick,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More Options",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier

    )
}

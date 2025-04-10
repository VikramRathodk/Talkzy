package com.devvikram.talkzy.ui.screens.personalChatroom.profile


import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.reuseables.ActionCard
import com.devvikram.talkzy.ui.reuseables.Avatar
import com.devvikram.talkzy.ui.reuseables.SectionHeader
import com.devvikram.talkzy.ui.reuseables.SettingsItem
import com.devvikram.talkzy.ui.reuseables.messages.SharedMediaGrid
import com.devvikram.talkzy.ui.screens.personalChatroom.PersonalProfileViewModel
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalProfileScreen(
    conversationId: String,
    appViewModel: AppViewModel,
    appLevelNavController: NavHostController,
    receiverId: String,
    personalProfileViewModel: PersonalProfileViewModel = hiltViewModel()
) {
    val userProfile by personalProfileViewModel.userProfile.collectAsStateWithLifecycle()

    LaunchedEffect(receiverId) {
        personalProfileViewModel.setUserId(receiverId)
    }

    // Custom scroll behavior with enhanced parallax effect (similar to group profile)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        snapAnimationSpec = tween(500)
    )

    val scrollFraction = scrollBehavior.state.collapsedFraction

    // Shared media mock data
    val sharedMediaList = listOf(
        "https://picsum.photos/200/300",
        "https://picsum.photos/201/300",
        "https://picsum.photos/202/300",
        "https://picsum.photos/203/300",
        "https://picsum.photos/204/300"
    )

    val headerHeight = 360.dp

    val avatarSize = 120.dp
    val avatarSizeCollapsed = 40.dp

    // Default profile data in case the actual data isn't loaded yet
    val contactName = userProfile?.name ?: "Username"
    val contactStatus = userProfile?.status ?: "Hey there! I'm using Talkzy"
    val isOnline = userProfile?.isOnline ?: false
    val profilePictureUrl = userProfile?.profilePicture ?: "https://randomuser.me/api/portraits/men/1.jpg"
    val lastSeen = userProfile?.lastSeen ?: 0L
    val gradientBackground = Brush.linearGradient(
        colors = listOf(
            colorScheme.primary.copy(alpha = 0.8f),
            colorScheme.surfaceVariant.copy(alpha = 0.6f),
            colorScheme.background
        ),
        start = Offset(0f, 0f),
        end = Offset(900f, 900f)
    )


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    // Only show title in toolbar when the header is sufficiently collapsed
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.graphicsLayer {
                                // Only show when sufficiently collapsed
                                alpha = scrollFraction * 2f
                            }
                        ) {
                            // Avatar that appears when collapsed
                            Avatar(
                                url = profilePictureUrl,
                                size = avatarSizeCollapsed,
                                isOnline = isOnline,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Column {
                                Text(
                                    text = contactName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isOnline) "Online" else "Last seen ${formatLastSeen(lastSeen)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { appLevelNavController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle more actions */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = colorScheme.surface,
                    scrolledContainerColor = colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },

    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            gradientBackground
                        )
                        .height(headerHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                // Subtle parallax effect
                                val scrollDelta = scrollBehavior.state.contentOffset
                                translationY = scrollDelta * 0.3f
                                alpha = 1f - min(1f, max(0f, scrollFraction * 1.5f))
                            }
                    )

                    // Profile info that fades as toolbar collapses
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 24.dp)
                            .graphicsLayer {
                                // Make this disappear as the toolbar title appears
                                alpha = 1f - (scrollFraction * 2f)
                                // Don't render when not visible to prevent overlap with toolbar title
                                if (scrollFraction > 0.5f) {
                                    alpha = 0f
                                }
                            }
                    ) {
                        // Avatar that gets smaller as we scroll
                        val currentAvatarSize = lerp(avatarSize, avatarSize/2, scrollFraction)

                        Avatar(
                            url = profilePictureUrl,
                            size = currentAvatarSize,
                            isOnline = isOnline,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .graphicsLayer {
                                    val scale = 1f - (scrollFraction * 0.5f)
                                    scaleX = scale
                                    scaleY = scale
                                }
                        )

                        Text(
                            text = contactName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            if (isOnline) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.Green,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Online",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            } else {
                                Text(
                                    text = "Last seen ${formatLastSeen(lastSeen)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color =  MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            ContactActionButton(
                                icon = Icons.Default.Call,
                                label = "Call",
                                color = MaterialTheme.colorScheme.tertiary
                            )

                            ContactActionButton(
                                icon = Icons.Default.Call,
                                label = "Video",
                                color = MaterialTheme.colorScheme.primary
                            )

                            ContactActionButton(
                                icon = Icons.Default.Search,
                                label = "Search",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            // Contact Info Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Contact Info",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Phone number
                        ContactInfoItem(
                            icon = Icons.Default.Phone,
                            title = "Phone",
                            value = userProfile?.mobileNumber ?: "+1 (555) 123-4567",
                            actionIcon = Icons.Default.Call
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Email
                        ContactInfoItem(
                            icon = Icons.Default.Email,
                            title = "Email",
                            value = userProfile?.email ?: "username@example.com",
                            actionIcon = Icons.Default.Send
                        )
                    }
                }
            }

            // Shared Media Section
            if (sharedMediaList.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                        SectionHeader(
                            title = "Shared Media",
                            actionLabel = "See All",
                            onActionClick = { /* Handle see all media */ }
                        )

                        SharedMediaGrid(sharedMediaList)
                    }
                }
            }

            // Quick Actions
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    SectionHeader(title = "Quick Actions")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ActionCard(
                            title = "Mute",
                            icon = Icons.Default.Notifications,
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            title = "Block",
                            icon = Icons.Default.Close,
                            backgroundColor = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ActionCard(
                            title = "Files",
                            icon = Icons.Default.AccountBox,
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            title = "Starred",
                            icon = Icons.Default.Star,
                            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Settings
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(title = "Privacy & Settings")

                    SettingsItem(
                        title = "Encryption",
                        icon = Icons.Default.Lock
                    )

                    SettingsItem(
                        title = "Notifications",
                        icon = Icons.Default.Notifications
                    )

                    SettingsItem(
                        title = "Media Visibility",
                        icon = Icons.Default.FavoriteBorder
                    )

                    SettingsItem(
                        title = "Report Contact",
                        icon = Icons.Default.Warning,
                        isDestructive = true
                    )
                }
            }

        }
    }
}

// Helper function to format "last seen" time
fun formatLastSeen(timestamp: Long): String {
    if (timestamp == 0L) return "recently"

    val currentTime = System.currentTimeMillis()
    val diffMinutes = (currentTime - timestamp) / (1000 * 60)

    return when {
        diffMinutes < 1 -> "just now"
        diffMinutes < 60 -> "$diffMinutes minutes ago"
        diffMinutes < 60 * 24 -> "${diffMinutes / 60} hours ago"
        diffMinutes < 60 * 24 * 7 -> "${diffMinutes / (60 * 24)} days ago"
        else -> "a long time ago"
    }
}




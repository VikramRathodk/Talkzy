package com.devvikram.talkzy.ui.screens.groupchatroom.profile

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.devvikram.talkzy.ui.models.Member
import com.devvikram.talkzy.ui.reuseables.ActionCard
import com.devvikram.talkzy.ui.reuseables.Avatar
import com.devvikram.talkzy.ui.reuseables.InfoChip
import com.devvikram.talkzy.ui.reuseables.RoleBadge
import com.devvikram.talkzy.ui.reuseables.SectionHeader
import com.devvikram.talkzy.ui.reuseables.SettingsItem
import com.devvikram.talkzy.ui.reuseables.messages.SharedMediaGrid
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupProfileScreen(
    conversationId: String,
    appLevelNavController: NavHostController,
    groupProfileViewModel: GroupProfileViewModel,
) {
    val groupInformation by groupProfileViewModel.groupInformation.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        snapAnimationSpec = tween(500)
    )

    val scrollFraction = scrollBehavior.state.collapsedFraction

    val sharedMediaList = listOf(
        "https://picsum.photos/200/300",
        "https://picsum.photos/201/300",
        "https://picsum.photos/202/300",
        "https://picsum.photos/203/300",
        "https://picsum.photos/204/300"
    )

    val membersList = listOf(
        Member("Jane Cooper", "Admin", "https://randomuser.me/api/portraits/women/44.jpg"),
        Member("Cameron Williamson", "Moderator", "https://randomuser.me/api/portraits/men/5.jpg"),
        Member("Brooklyn Simmons", "Member", "https://randomuser.me/api/portraits/women/33.jpg"),
        Member("Robert Fox", "Member", "https://randomuser.me/api/portraits/men/78.jpg"),
        Member("Savannah Nguyen", "Member", "https://randomuser.me/api/portraits/women/67.jpg"),
        Member("Wade Warren", "Member", "https://randomuser.me/api/portraits/men/41.jpg")
    )

    val headerHeight = 360.dp

    val avatarSize = 120.dp
    val avatarSizeCollapsed = 40.dp

    val groupName = groupInformation?.conversation?.name ?: "Design Team"
    val groupDescription = groupInformation?.conversation?.description
        ?: "Our design team collaborates on all UI/UX projects across the company. Share ideas, feedback, and resources here."

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
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.graphicsLayer {
                                alpha = scrollFraction * 2f
                            }
                        ) {
                            Avatar(
                                url = "https://picsum.photos/600/300",
                                size = avatarSizeCollapsed,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Column {
                                Text(
                                    text = groupName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "${membersList.size} members",
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
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
            // Group Header
            // Group Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                ) {
                    // Gradient background instead of image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                              gradientBackground
                            )
                    )

                    // Group info that fades as toolbar collapses
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 24.dp)
                            .graphicsLayer {
                                alpha = 1f - (scrollFraction * 2f)
                                if (scrollFraction > 0.5f) {
                                    alpha = 0f
                                }
                            }
                    ) {
                        val currentAvatarSize = lerp(avatarSize, avatarSize / 2, scrollFraction)

                        Avatar(
                            url = "https://picsum.photos/600/300",
                            size = currentAvatarSize,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .graphicsLayer {
                                    val scale = 1f - (scrollFraction * 0.5f)
                                    scaleX = scale
                                    scaleY = scale
                                }
                        )

                        Text(
                            text = groupName,
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
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${membersList.size} members",
                                style = MaterialTheme.typography.bodyMedium,
                                color =  MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* Handle add member */ },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = SolidColor(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    )
                                )
                            ) {
                                Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Member")
                            }

                            OutlinedButton(
                                onClick = { /* Handle share */ },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor =  MaterialTheme.colorScheme.primary
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = SolidColor(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    )
                                )
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share")
                            }
                        }
                    }
                }
            }


            // Group Info Card
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
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "About",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = groupDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoChip(label = "Created Apr 2025", icon = Icons.Default.DateRange)
                            InfoChip(label = "Public Group", icon = Icons.Default.Person)
                        }
                    }
                }
            }

            // Members Section
            item {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    SectionHeader(
                        title = "Members",
                        actionLabel = "See All",
                        onActionClick = { /* Handle see all members */ }
                    )

                    MembersGrid(membersList)
                }
            }

            // Shared Media Section with neat grid
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

            // Quick Actions (replaces some settings items)
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
                            title = "Search",
                            icon = Icons.Default.Search,
                            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
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
                            icon = Icons.Default.MailOutline,
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            title = "Poll",
                            icon = Icons.Default.AccountBox,
                            backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Settings Section (streamlined)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(title = "Settings")

                    SettingsItem(
                        title = "Group Privacy",
                        icon = Icons.Default.Lock
                    )

                    SettingsItem(
                        title = "Notifications",
                        icon = Icons.Default.Notifications
                    )

                    SettingsItem(
                        title = "Media & Files",
                        icon = Icons.Default.Info
                    )

                    SettingsItem(
                        title = "Permissions",
                        icon = Icons.Default.Lock
                    )

                    SettingsItem(
                        title = "Leave Group",
                        icon = Icons.Default.ExitToApp,
                        isDestructive = true
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(88.dp)) // Space for FAB
            }
        }
    }
}










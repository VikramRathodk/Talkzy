package com.devvikram.talkzy.ui.screens.groupchatroom.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage

@Composable
fun GroupProfileScreen(
    conversationId: String,
    appLevelNavController: NavHostController,
    groupProfileViewModel: GroupProfileViewModel,
) {
    val groupInformation by groupProfileViewModel.groupInformation.collectAsState()
    val membersList = listOf<String>(
        "Member 1",
        "Member 2",
        "Member 3",
        "Member 4",
        "Member 5",
        "Member 6",
        "Member 7",
        "Member 8",
        "Member 9",
        "Member 10",
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Collapsing Header with Group Name and Avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Avatar(url = "" ?: "", size = 100.dp)
                Text(
                    text = "Dummy Group Name" ?: "Group Name",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${membersList.size} Members",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Group Members Section
        Text(
            text = "Members",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(membersList) { member ->
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Avatar(url = "", size = 60.dp)
                    Text(text = "Dummy name", fontSize = 12.sp)
                    RoleBadge(role = "Dummnt role")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Group Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("About the Group", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = groupInformation?.conversation?.description ?: "No description yet.", fontSize = 14.sp)
            }
        }

        // Settings Section
        SettingsItem("Mute Notifications")
        SettingsItem("Leave Group")
        SettingsItem("Report Group")

        // Shared Media (Mock placeholder)
        Text(
            text = "Shared Media",
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SettingsItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 14.sp, modifier = Modifier.weight(1f))
        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun Avatar(url: String, size: Dp) {
    AsyncImage(
        model = url,
        contentDescription = "Avatar",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun RoleBadge(role: String) {
    val badgeColor = when (role.lowercase()) {
        "admin" -> MaterialTheme.colorScheme.primary
        "moderator" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.secondary
    }

    Text(
        text = role.replaceFirstChar { it.uppercase() },
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(badgeColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
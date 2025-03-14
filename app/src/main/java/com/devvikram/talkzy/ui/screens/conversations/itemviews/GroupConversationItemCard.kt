package com.devvikram.talkzy.ui.screens.conversations.itemviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.talkzy.R
import com.devvikram.talkzy.config.constants.MediaType
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.reuseables.CircularBadge
import com.devvikram.talkzy.ui.reuseables.ProfileImage
import com.devvikram.talkzy.ui.screens.conversations.ConversationItem

@Composable
fun GroupConversationItemCard(
    conversationItem: ConversationItem.GroupConversation,
    modifier: Modifier = Modifier,
    homeNavigationController: NavHostController,
    onClick: () -> Unit,
) {
    val lastMessage = conversationItem.lastMessage

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            imagePath = conversationItem.groupIconLocalPath,
            modifier = Modifier.size(48.dp),
            placeholderRes = R.drawable.group_icon
        )




        Spacer(modifier = Modifier.width(12.dp))

        // Conversation Details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            // Group Name and Time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversationItem.groupTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = formatTimestamp(conversationItem.timeStamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Last Message Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Media Icon if message contains an image or video
                if (lastMessage.mediaType == MediaType.IMAGE.name) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Image",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                } else if (lastMessage.mediaType == MediaType.VIDEO.name) {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = "Video",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Last Message Preview
                Text(
                    text = lastMessage.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    modifier = Modifier.weight(1f)
                )

                // Unread Count Badge
                CircularBadge(
                    unreadCount = 10,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

    }
}


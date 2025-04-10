package com.devvikram.talkzy.ui.screens.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.data.room.models.RoomContact
import com.devvikram.talkzy.ui.reuseables.Avatar

@Composable
fun ContactItem(
    isSelected: Boolean = false,
    contact: RoomContact,
    navigateToPersonalChatroom: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable(onClick = navigateToPersonalChatroom),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Avatar(
                    url = contact.profilePicture,
                    size = 44.dp,
                    modifier = Modifier
                        .border(
                            width = if (isSelected) 1.5.dp else 0.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )

                if (contact.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(1.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (contact.isOnline) "Online • ${contact.mobileNumber}" else contact.mobileNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



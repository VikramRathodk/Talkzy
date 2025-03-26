package com.devvikram.talkzy.ui.screens.personalChatroom.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.data.room.models.RoomContact

@Composable
fun ProfileDetailsSection(user: RoomContact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ProfileDetailItem(
                icon = Icons.Default.Email,
                label = "Email",
                value = user.email ?: "Not available",
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                valueColor = MaterialTheme.colorScheme.onBackground
            )
            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .alpha(0.3f),
                color = MaterialTheme.colorScheme.outline
            )
            ProfileDetailItem(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = user.mobileNumber,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                valueColor = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
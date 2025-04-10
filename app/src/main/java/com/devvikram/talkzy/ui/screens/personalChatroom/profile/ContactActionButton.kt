package com.devvikram.talkzy.ui.screens.personalChatroom.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContactActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalIconButton(
            onClick = { /* Handle action */ },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = color.copy(alpha = 0.9f)
            )
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.surface
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color =  MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
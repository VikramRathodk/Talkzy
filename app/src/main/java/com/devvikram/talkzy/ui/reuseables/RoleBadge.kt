package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleBadge(role: String) {
    val (badgeColor, textColor) = when (role.lowercase()) {
        "admin" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        "moderator" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    }

    Surface(
        color = badgeColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.height(22.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = role.replaceFirstChar { it.uppercase() },
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
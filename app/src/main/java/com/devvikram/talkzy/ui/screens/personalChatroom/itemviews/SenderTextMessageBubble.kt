package com.devvikram.talkzy.ui.screens.personalChatroom.itemviews

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem

@Composable
fun SenderTextMessageBubble(message: PersonalChatMessageItem.SenderTextMessageItem) {
    val time = AppUtils.getTimeFromTimeStamp(message.timestamp)

    val icon = when {
        message.isReadBy.isNotEmpty() -> Icons.Default.CheckCircle // Read
        message.isReceivedBy.isNotEmpty() -> Icons.Default.Check // Delivered
        else -> Icons.Default.Favorite // Sent (Pending)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .wrapContentWidth()
                .widthIn(min = 80.dp ,max = 250.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF0A1950), Color(0xFF4A6EA8))
                    ),
                    shape =   RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp)
                )

                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy (
                    space = 4.dp,
                ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = icon,
                        contentDescription = "Message status",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}





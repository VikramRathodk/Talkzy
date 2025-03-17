package com.devvikram.talkzy.ui.screens.personalChatroom.itemviews

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem

@Composable
fun SenderTextMessageBubble(
    message: PersonalChatMessageItem.SenderTextMessageItem
) {
    val time = AppUtils.getTimeFromTimeStamp(message.timestamp)
    var isExpanded by remember { mutableStateOf(false) }

    val icon = when {
        message.isReadBy.isNotEmpty() -> Icons.Default.CheckCircle // Read
        message.isReceivedBy.isNotEmpty() -> Icons.Default.Check // Delivered
        else -> Icons.Default.CheckCircle // Sent (Pending)
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
                .widthIn(min = 80.dp, max = 250.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF0A1950), Color(0xFF4A6EA8))
                    ),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp
                    )
                )

                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                ),
                horizontalAlignment = Alignment.Start
            ) {
                val maxLines = if (isExpanded) Int.MAX_VALUE else 5 // Show limited lines if not expanded

                Text(
                    text = message.text,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )

                if (message.text.length > 150) {
                    Text(
                        text = if (isExpanded) "Read Less" else "Read More",
                        color = Color.LightGray,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .clickable { isExpanded = !isExpanded }
                            .padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )

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

@Preview
@Composable
fun PreviewSenderTextMessageBubble() {
    SenderTextMessageBubble(
        message = PersonalChatMessageItem.SenderTextMessageItem(
            text = "Hello, this is a sample text message.",
            timestamp = 1670703200,
            isReadBy = mapOf("user1" to 1670703200,),
            isReceivedBy = mapOf("user2" to 90408239840L),
            messageId ="",
            conversationId = "",
            senderId ="dskjflkjaoisdufowiueodlfjls",
            senderName = "Vikram Rathod",

        )
    )
}





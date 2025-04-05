package com.devvikram.talkzy.ui.screens.groupchatroom.itemviews

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.R
import com.devvikram.talkzy.ui.reuseables.messages.MessageTimestamp
import com.devvikram.talkzy.ui.reuseables.messages.SenderMessageBubbleWrapper
import com.devvikram.talkzy.ui.screens.groupchatroom.GroupChatMessageItem


@Composable
fun GroupSenderTextMessageBubble(
    message: GroupChatMessageItem.SenderTextMessageItem
) {
    val time = AppUtils.getTimeFromTimeStamp(message.timestamp)
    var isExpanded by remember { mutableStateOf(false) }

    val statusIconRes = when {
        message.isReadBy.isNotEmpty() -> R.drawable.ic_read_tick
        message.isReceivedBy.isNotEmpty() -> R.drawable.ic_delivered_tick
        else -> R.drawable.ic_sent_tick
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        SenderMessageBubbleWrapper(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 250.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.wrapContentWidth()
            ) {
                val maxLines = if (isExpanded) Int.MAX_VALUE else 5

                Text(
                    text = message.text,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.wrapContentWidth()
                )

                if (message.text.length > 150) {
                    Text(
                        text = if (isExpanded) "Read Less" else "Read More",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .clickable { isExpanded = !isExpanded }
                            .align(Alignment.End)
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MessageTimestamp(time = time)

                    Icon(
                        painter = painterResource(statusIconRes),
                        contentDescription = "Message status",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}



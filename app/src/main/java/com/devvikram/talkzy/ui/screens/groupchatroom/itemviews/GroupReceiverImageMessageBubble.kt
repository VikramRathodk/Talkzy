package com.devvikram.talkzy.ui.screens.groupchatroom.itemviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.ui.reuseables.messages.receiver.ReceiverMessageBubbleWrapper
import com.devvikram.talkzy.ui.screens.groupchatroom.GroupChatMessageItem
import com.devvikram.talkzy.ui.screens.groupchatroom.SenderInfoSection

@Composable
fun GroupReceiverImageMessageBubble(
    message: GroupChatMessageItem.ReceiverImageMessageItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 48.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        AsyncImage(
            model = message.senderAvatarUrl,
            contentDescription = "Sender Avatar",
            error = painterResource(id = com.devvikram.talkzy.R.drawable.baseline_person_24),
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        ReceiverMessageBubbleWrapper(
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column {
                // Sender Name
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Image
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = "Received Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Timestamp
                Text(
                    text = AppUtils.getTimeFromTimeStamp(message.timestamp),
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}


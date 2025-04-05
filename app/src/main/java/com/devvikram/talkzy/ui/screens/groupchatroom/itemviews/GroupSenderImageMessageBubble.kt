package com.devvikram.talkzy.ui.screens.groupchatroom.itemviews


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.ui.reuseables.messages.MessageTimestamp
import com.devvikram.talkzy.ui.reuseables.messages.SenderMessageBubbleWrapper
import com.devvikram.talkzy.ui.screens.groupchatroom.GroupChatMessageItem

@Composable
fun GroupSenderImageMessageBubble(
    message: GroupChatMessageItem.SenderImageMessageItem
) {
    val time = AppUtils.getTimeFromTimeStamp(message.timestamp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        SenderMessageBubbleWrapper {
            Column {
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = "Sent Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                MessageTimestamp(time = time)
            }
        }
    }
}


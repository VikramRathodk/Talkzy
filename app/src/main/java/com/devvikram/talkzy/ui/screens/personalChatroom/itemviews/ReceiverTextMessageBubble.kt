package com.devvikram.talkzy.ui.screens.personalChatroom.itemviews

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.devvikram.talkzy.AppUtils
import com.devvikram.talkzy.ui.reuseables.messages.receiver.ReceiverMessageBubbleWrapper
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiverTextMessageBubble(message: PersonalChatMessageItem.ReceiverTextMessageItem) {
    var isExpanded by remember { mutableStateOf(false) }
    val maxLines = if (isExpanded) Int.MAX_VALUE else 4
    val showToggle = message.text.length > 200 || message.text.count { it == '\n' } > 4

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 48.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {

        ReceiverMessageBubbleWrapper(
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = maxLines
                )

                if (showToggle) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isExpanded) "Read less" else "Read more",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 2.dp)
                            .clickable { isExpanded = !isExpanded }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

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

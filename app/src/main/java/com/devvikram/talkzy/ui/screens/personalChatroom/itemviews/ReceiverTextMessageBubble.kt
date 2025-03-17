package com.devvikram.talkzy.ui.screens.personalChatroom.itemviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiverTextMessageBubble(message: PersonalChatMessageItem.ReceiverTextMessageItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Column {
//                Text(
//                    text = message.senderName,
//                    style = TextStyle(fontSize = 12.sp, color = Color.Gray),
//                    modifier = Modifier.padding(bottom = 4.dp)
//                )
                Text(
                    text = message.text,
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
                Text(
                    text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp)),
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

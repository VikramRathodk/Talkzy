package com.devvikram.talkzy.ui.screens.personalChatroom.itemviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.ui.screens.personalChatroom.models.PersonalChatMessageItem

@Composable
fun SenderTextMessageBubble(message: PersonalChatMessageItem.SenderTextMessageItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = message.text,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

package com.devvikram.talkzy.ui.screens.personalChatroom.itemviews

import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
@Composable
fun TypingIndicatorBubble() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Text(text = "Typing...", color = Color.White, fontStyle = FontStyle.Italic)
    }
}

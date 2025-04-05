package com.devvikram.talkzy.ui.reuseables.messages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageTimestamp(time: String) {
    Text(
        text = time,
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier
            .padding(top = 4.dp, end = 2.dp)
    )
}




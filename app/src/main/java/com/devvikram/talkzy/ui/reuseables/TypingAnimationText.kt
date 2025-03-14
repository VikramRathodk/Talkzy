package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color


@Composable
fun TypingAnimationText(
    fullText: String,
    delayMillis: Int = 100,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )

) {
    var textLength by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(fullText) {
        while (true) {
            // Typing Effect
            for (i in fullText.indices) {
                coroutineScope.launch {
                    delay(delayMillis.toLong())
                    textLength = i + 1
                }
            }
            delay(1000) // Pause before erasing

            // Erasing Effect
            for (i in fullText.length downTo 0) {
                coroutineScope.launch {
                    delay(delayMillis.toLong())
                    textLength = i
                }
            }
            delay(500) // Pause before restarting
        }
    }

    Row {
        fullText.take(textLength).forEachIndexed { index, char ->
            Text(
                text = char.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = colors[index % colors.size],
                maxLines = 1,
            )
        }
    }
}


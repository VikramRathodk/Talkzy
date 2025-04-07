package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.devvikram.talkzy.R

@Composable
fun Avatar(
    url: String,
    size: Dp,
    isOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(size),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .error(R.drawable.baseline_person_24)
                    .crossfade(true)
                    .build(),
                contentDescription = "Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Online indicator
        if (isOnline) {
            Surface(
                shape = CircleShape,
                color = Color.Green,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .size(size / 5)
                    .align(Alignment.BottomEnd)
            ) {}
        }
    }
}
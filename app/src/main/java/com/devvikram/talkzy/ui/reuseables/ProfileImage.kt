package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.devvikram.talkzy.R

@Composable
fun ProfileImage(
    imagePath: String?,
    modifier: Modifier = Modifier,
    placeholderRes: Int = R.drawable.baseline_person_24,
    contentDescription: String = "Profile Picture",
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
        ),
        start = Offset(0f, 0f),
        end = Offset(100f, 100f)
    )


    val shape = CircleShape

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(shape)
            .background(brush = gradientBrush)
            .border(
                width = 0.25.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = shape
            ),
         contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imagePath.takeIf { !it.isNullOrEmpty() })
                .error(placeholderRes)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
            ,
            contentScale = ContentScale.Crop
        )
    }
}



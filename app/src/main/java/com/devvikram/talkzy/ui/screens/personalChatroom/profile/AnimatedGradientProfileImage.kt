package com.devvikram.talkzy.ui.screens.personalChatroom.profile

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.devvikram.talkzy.R

@Composable
fun AnimatedGradientProfileImage(
    profilePicturePath: String?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Profile Image Transition")

    val gradient by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primaryContainer,
        targetValue = MaterialTheme.colorScheme.secondary,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Gradient Color"
    )

    Box(
        modifier = modifier
            .size(160.dp)
            .border(
                width = 4.dp,
                brush = Brush.sweepGradient(
                    colors = listOf(
                        gradient,
                        MaterialTheme.colorScheme.primaryContainer,
                        gradient
                    )
                ),
                shape = CircleShape
            )
            .padding(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePicturePath)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.person_icon_3d),
            error = painterResource(R.drawable.person_icon_3d),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = gradient,
                    spotColor = gradient
                ),
            contentScale = ContentScale.Crop
        )
    }
}
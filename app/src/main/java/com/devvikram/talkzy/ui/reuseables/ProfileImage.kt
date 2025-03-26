package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.error
import com.devvikram.talkzy.R

@Composable
fun ProfileImage(
    imagePath: String?,
    modifier: Modifier = Modifier,
    placeholderRes: Int = R.drawable.person_icon_3d,
    contentDescription: String = "Profile Picture",
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePath.takeIf { !it.isNullOrEmpty() })
            .error(placeholderRes)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop
    )
}

package com.devvikram.talkzy.ui.screens.groupchatroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.reuseables.ProfileImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatTopBar(
    onBackPressed: () -> Unit,
    onClick: () -> Unit,
    groupChatViewmodel: GroupChatViewmodel
) {
    val groupInformationState = groupChatViewmodel.groupInformation.collectAsStateWithLifecycle()
    val groupInfo = groupInformationState.value

    TopAppBar(
        title = {
            Row (
                modifier = Modifier.clickable(
                    onClick = onClick
                ),
                verticalAlignment = Alignment.CenterVertically

            ){
                ProfileImage(
                    imagePath = "",
                    modifier = Modifier.padding(end = 8.dp)
                )

                Column {
                    Text(
                        text = groupInfo?.conversation?.name.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                    if (groupInfo != null && groupInfo.conversation.participantIds.isNotEmpty()) {
                        Text(
                            text = "${groupInfo.conversation.participantIds.size} participants",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(
                onClick = { }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    )
}

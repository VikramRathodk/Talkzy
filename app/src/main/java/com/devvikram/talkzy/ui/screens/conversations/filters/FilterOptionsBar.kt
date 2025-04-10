package com.devvikram.talkzy.ui.screens.conversations.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.ui.screens.conversations.ConversationViewmodel


@Composable
fun FilterOptionsBar(
    conversationViewmodel: ConversationViewmodel
) {
    val filters = conversationViewmodel.filterOptionList.collectAsState()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(filters.value) { option->
            FilterOptionItem(
                filterOption = option,
                onClick = { it
                    conversationViewmodel.setSelection(it)
                }
            )

        }

    }
}
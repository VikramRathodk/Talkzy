package com.devvikram.talkzy.ui.screens.conversations.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.ui.models.FilterOption

@Composable
fun FilterOptionItem(
    filterOption: FilterOption,
    onClick: (FilterOption) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable { onClick(filterOption) },
        color = if (filterOption.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small,
        tonalElevation = if (filterOption.isSelected) 4.dp else 0.dp,
        shadowElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = filterOption.name,
                style = MaterialTheme.typography.labelLarge,
                color = if (filterOption.isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

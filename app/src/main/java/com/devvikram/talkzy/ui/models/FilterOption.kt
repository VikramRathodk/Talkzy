package com.devvikram.talkzy.ui.models

import androidx.compose.runtime.Composable

data class FilterOption(
    val name: String = "",
    val isSelected: Boolean = false,
    val icon: @Composable () -> Unit,
)

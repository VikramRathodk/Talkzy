package com.devvikram.talkzy.ui.screens.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun LogoutConfirmationDialog (
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,

){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text("Logout") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("Cancel")
            }
        }
    )
}
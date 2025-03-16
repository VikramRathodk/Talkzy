package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.R

@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    onMessageSend: (String) -> Unit,
    onCameraClick: () -> Unit,
    onAttachmentClick: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(50.dp))
            .border(
                border = BorderStroke(0.1.dp, Color.LightGray),
                shape = RoundedCornerShape(24.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Camera Icon
        IconButton(onClick = onCameraClick, modifier = Modifier
            .size(32.dp)
            .padding(start = 8.dp)) {
            Icon(
                painter = painterResource(R.drawable.camera_icon),
                contentDescription = "Open Camera",
                tint = Color.Gray
            )
        }

        // Text Input Field
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp),
            placeholder = { Text("Type a message...", color = Color.Gray) },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (messageText.isNotBlank()) {
                        onMessageSend(messageText)
                        messageText = ""
                    }
                }
            ),
            maxLines = 5
        )

        // Attachment Icon
        IconButton(onClick = onAttachmentClick, modifier = Modifier.size(32.dp)) {
            Icon(
                painter = painterResource(
                    R.drawable.attachment_icon
                ),
                contentDescription = "Attach File",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        // Send Button
        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onMessageSend(messageText)
                    messageText = ""
                }
            },
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
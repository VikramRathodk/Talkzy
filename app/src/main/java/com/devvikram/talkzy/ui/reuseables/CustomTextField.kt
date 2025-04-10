package com.devvikram.talkzy.ui.reuseables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                color = colorScheme.onSurface.copy(alpha = 0.7f)
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.onSurface
            )
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onPasswordToggle?.invoke() }) {
                    Icon(
                        painter = painterResource(
                            id = if (isPasswordVisible) R.drawable.hide_visibility else R.drawable.show_visibility
                        ),
                        contentDescription = null,
                        tint = colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        } else null,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = colorScheme.onSurface),
        colors = TextFieldDefaults.colors(
            focusedTextColor = colorScheme.onSurface,
            unfocusedTextColor = colorScheme.onSurface,
            focusedContainerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f),
            unfocusedContainerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = colorScheme.primary
        ),
        visualTransformation = if (isPassword && !isPasswordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None
    )
}

package com.devvikram.talkzy.ui.screens.authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.navigation.OnboardingDestination
import com.devvikram.talkzy.ui.reuseables.CustomTextField


@Composable
fun RegisterScreen(
    appViewModel: AppViewModel,
    navController: NavController,
    authenticationViewModel: AuthenticationViewmodel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val signUpState by authenticationViewModel.signUpState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(signUpState) {
        if (signUpState is AuthenticationViewmodel.SignUpState.Success) {
            Toast.makeText(
                context,
                (signUpState as AuthenticationViewmodel.SignUpState.Success).message,
                Toast.LENGTH_SHORT
            ).show()

            navController.navigate(OnboardingDestination.Login.route)
        }
        if (signUpState is AuthenticationViewmodel.SignUpState.Error) {
            Toast.makeText(
                context,
                (signUpState as AuthenticationViewmodel.SignUpState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val gradientBackground = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.surface
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.2f),
            tonalElevation = 48.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Create Account",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.background
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Sign up to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(24.dp))

                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
                    icon = Icons.Default.AccountCircle
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    icon = Icons.Default.Email
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordToggle = { isPasswordVisible = !isPasswordVisible })
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        authenticationViewModel.signUp(
                            fullName = name,
                            email = email,
                            password = password,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))
                ) {
                    if (signUpState is AuthenticationViewmodel.SignUpState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Sign Up", fontSize = 18.sp, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    Text("Already have an account?", color = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Sign In",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { navController.navigate(OnboardingDestination.Login.route) })
                }
            }
        }
    }
}


package com.devvikram.talkzy.ui.screens.onboarding

import androidx.compose.runtime.Composable
import com.devvikram.talkzy.ui.navigation.OnboardingDestination
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to Talkzy!")
        Button(onClick = { navController.navigate(OnboardingDestination.Onboarding1.route) }) {
            Text("Get Started")
        }
    }
}
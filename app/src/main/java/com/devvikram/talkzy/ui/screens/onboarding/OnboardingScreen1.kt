package com.devvikram.talkzy.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.devvikram.talkzy.ui.navigation.OnboardingDestination

@Composable
fun OnboardingScreen1(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Onboarding Screen 1")
        Button(onClick = { navController.navigate(OnboardingDestination.Onboarding2.route) }) {
            Text("Next")
        }
    }
}

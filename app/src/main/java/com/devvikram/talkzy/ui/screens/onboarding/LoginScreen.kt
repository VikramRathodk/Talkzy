package com.devvikram.talkzy.ui.screens.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.navigation.OnboardingDestination

@Composable
fun LoginScreen(appViewModel: AppViewModel, navController: NavController) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Sign In",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            appViewModel.loginPreference.setLoggedIn(true)
                            appViewModel._isLoggedIn.value = true
                        }
                )
                Button(onClick = { navController.navigate(OnboardingDestination.Register.route) }) {
                    Text("Register")
                }
            }
        }
    }
}

package com.devvikram.talkzy.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.navigation.OnboardingDestination


@Composable
fun OnboardingNavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel,
    isOnboardingCompleted: Boolean?,
    onboardingFinished: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (isOnboardingCompleted == true) {
            OnboardingDestination.Login.route
        } else
            OnboardingDestination.Welcome.route
    ) {
        composable(OnboardingDestination.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(OnboardingDestination.Onboarding1.route) {
            OnboardingScreen1(navController)
        }
        composable(OnboardingDestination.Onboarding2.route) {
            OnboardingScreen2(navController)
        }
        composable(OnboardingDestination.Onboarding3.route) {
            OnboardingScreen3(navController,onboardingFinished)
        }
        composable(OnboardingDestination.Login.route) {
            LoginScreen(appViewModel = appViewModel, navController = navController)
        }
        composable(OnboardingDestination.Register.route) {
            RegisterScreen(appViewModel = appViewModel, navController = navController)
        }
    }
}




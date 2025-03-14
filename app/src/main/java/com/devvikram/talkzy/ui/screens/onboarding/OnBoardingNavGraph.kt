package com.devvikram.talkzy.ui.screens.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.navigation.OnboardingDestination
import com.devvikram.talkzy.ui.screens.authentication.LoginScreen
import com.devvikram.talkzy.ui.screens.authentication.RegisterScreen


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
            OnboardingDestination.OnBoarding.route
    ) {
        composable(
            route = OnboardingDestination.OnBoarding.route,
            enterTransition = {
                slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(700)) + fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it / 2 }, animationSpec = tween(700)) + fadeOut(animationSpec = tween(700))
            }
        ) {
            OnboardingScreen(navController, appViewModel, onboardingFinished)
        }
        composable(
            route = OnboardingDestination.Login.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(800)) + fadeIn(animationSpec = tween(800))
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f, animationSpec = tween(800)) + fadeOut(animationSpec = tween(800))
            }
        ) {
            LoginScreen(appViewModel = appViewModel, navController = navController)
        }
        composable(
            route = OnboardingDestination.Register.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it / 2 }, animationSpec = tween(900)) + fadeIn(animationSpec = tween(900))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it / 2 }, animationSpec = tween(900)) + fadeOut(animationSpec = tween(900))
            }
        ) {
            RegisterScreen(appViewModel = appViewModel, navController = navController)
        }
    }
}




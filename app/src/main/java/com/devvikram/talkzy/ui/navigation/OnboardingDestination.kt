package com.devvikram.talkzy.ui.navigation


import kotlinx.serialization.Serializable

@Serializable
sealed class OnboardingDestination(val route: String) {

    @Serializable
    data object OnBoarding : OnboardingDestination(route = "welcome")

    @Serializable
    data object Login : OnboardingDestination(route = "login")

    @Serializable
    data object Register : OnboardingDestination(route = "register")
}

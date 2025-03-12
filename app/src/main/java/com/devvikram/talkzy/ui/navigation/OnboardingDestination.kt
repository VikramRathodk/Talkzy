package com.devvikram.talkzy.ui.navigation


import kotlinx.serialization.Serializable

@Serializable
sealed class OnboardingDestination(val route: String) {
    @Serializable
    data object Welcome : OnboardingDestination("welcome")
    @Serializable
    data object Onboarding1 : OnboardingDestination("onboarding_1")
    @Serializable
    data object Onboarding2 : OnboardingDestination("onboarding_2")
    @Serializable
    data object Onboarding3 : OnboardingDestination("onboarding_3")

    @Serializable
    data object Login : OnboardingDestination("login")
    @Serializable
    data object Register : OnboardingDestination("register")
}

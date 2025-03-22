package com.devvikram.talkzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.navigation.OnboardingDestination
import com.devvikram.talkzy.ui.screens.groupchatroom.GroupChatroomScreen
import com.devvikram.talkzy.ui.screens.home.HomeScreen
import com.devvikram.talkzy.ui.screens.onboarding.OnboardingNavGraph
import com.devvikram.talkzy.ui.screens.personalChatroom.PersonalChatroomScreen
import com.devvikram.talkzy.ui.screens.personalChatroom.PersonalProfileScreen
import com.devvikram.talkzy.ui.theme.TalkzyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TalkzyTheme {
                appViewModel.listenToContacts()
                appViewModel.listenToConversation()
                AppScreen(modifier = Modifier, viewModel = appViewModel)
            }
        }
    }
}

@Composable
fun AppScreen(modifier: Modifier, viewModel: AppViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(initial = false)
    val navController = rememberNavController()
    val isOnboardingCompleted by viewModel.isOnBoardingCompleted.observeAsState(initial = false)

    if (isLoggedIn) {
        AppNavigationScreen(viewModel, navController, modifier)
    } else {
        OnBoardingScreen(viewModel, navController, isOnboardingCompleted)
    }
}

@Composable
fun OnBoardingScreen(
    viewModel: AppViewModel,
    navController: NavHostController,
    isOnBoardingCompleted: Boolean?
) {
    OnboardingNavGraph(
        appViewModel = viewModel,
        navController = navController,
        isOnboardingCompleted = isOnBoardingCompleted,
        onboardingFinished = { viewModel.loginPreference.setOnBoardingCompleted(true) }
    )
}

@Composable
fun AppNavigationScreen(
    viewModel: AppViewModel,
    navController: NavHostController,
    modifier: Modifier
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeNavigationDestination.HomeDest.route,
    ) {
        composable(
            route = HomeNavigationDestination.HomeDest.route,
        ) {
            HomeScreen(
                appViewmodel = viewModel,
                appLevelNavController = navController,
            )
        }
        composable(
            route = HomeNavigationDestination.PersonalChatroomDest("0", "0").route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("receiverId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val conversationId =
                backStackEntry.arguments?.getString("conversationId") ?: return@composable
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: return@composable

            PersonalChatroomScreen(
                conversationId = conversationId,
                appViewmodel = viewModel,
                appLevelNavController = navController,
                receiverId = receiverId
            )
        }

        composable(
            route = HomeNavigationDestination.PersonalProfileDest("0", "0").route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("receiverId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val conversationId =
                backStackEntry.arguments?.getString("conversationId") ?: return@composable
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: return@composable

            PersonalProfileScreen(
                conversationId = conversationId,
                appViewmodel = viewModel,
                appLevelNavController = navController,
                receiverId = receiverId
            )
        }


        composable(route = HomeNavigationDestination.GroupChatroomDest.route) {
            GroupChatroomScreen(
                appViewmodel = viewModel,
                appLevelNavController = navController,
            )
        }
    }
}



package com.devvikram.talkzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.screens.creategroup.CreateGroupScreen
import com.devvikram.talkzy.ui.screens.groupchatroom.GroupChatroomScreen
import com.devvikram.talkzy.ui.screens.groupchatroom.profile.GroupProfileScreen
import com.devvikram.talkzy.ui.screens.groupchatroom.profile.GroupProfileViewModel
import com.devvikram.talkzy.ui.screens.home.HomeScreen
import com.devvikram.talkzy.ui.screens.onboarding.OnboardingNavGraph
import com.devvikram.talkzy.ui.screens.personalChatroom.PersonalChatroomScreen
import com.devvikram.talkzy.ui.screens.personalChatroom.profile.PersonalProfileScreen
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
        viewModel.listenToContacts()
        viewModel.listenToConversation()
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
                appViewModel = viewModel,
                appLevelNavController = navController,
                receiverId = receiverId,
            )
        }

        composable(
            route = HomeNavigationDestination.GroupProfile("0").route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
            )
        ) {
            backStackEntry ->
            val groupProfileViewModel = hiltViewModel<GroupProfileViewModel>()
            val conversationId =
                backStackEntry.arguments?.getString("conversationId") ?: return@composable
            GroupProfileScreen(
                conversationId = conversationId,
                appLevelNavController = navController,
                groupProfileViewModel  = groupProfileViewModel,
            )
        }


        composable(
            route = HomeNavigationDestination.GroupChatroomDest("0").route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val conversationId =
                backStackEntry.arguments?.getString("conversationId") ?: return@composable

            GroupChatroomScreen(
                appViewmodel = viewModel,
                appLevelNavController = navController,
                conversationId = conversationId,
            )
        }

        composable(route = HomeNavigationDestination.CreateGroupDest.route) {
            CreateGroupScreen(
                appViewmodel = viewModel,
                appLevelNavController = navController,
            )
        }
    }
}



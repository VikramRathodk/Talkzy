package com.devvikram.talkzy.ui.screens.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.screens.HomeViewmodel
import com.devvikram.talkzy.ui.screens.contacts.ContactScreen
import com.devvikram.talkzy.ui.screens.contacts.ContactViewmodel
import com.devvikram.talkzy.ui.screens.conversations.ConversationViewmodel
import com.devvikram.talkzy.ui.screens.conversations.ConversationsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appViewmodel: AppViewModel,
    homeViewmodel: HomeViewmodel = hiltViewModel(),
    appLevelNavController: NavHostController
) {
    val homeNavigationController = rememberNavController()
    val isHomeToolbarVisibleFlow =
        homeViewmodel.isHomeToolbarVisibleFlow.collectAsState(initial = true).value
    val currentDestination =
        homeNavigationController.currentBackStackEntryAsState().value?.destination

    val loggedUser = homeViewmodel.loggedUser.collectAsState(initial = null).value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isHomeToolbarVisibleFlow) {
                HomeToolbar(
                    appViewModel = appViewmodel,
                    homeNavigationController = homeNavigationController,
                    currentDestination = currentDestination,
                    loggedUser = loggedUser
                )
            }
        },
        bottomBar = {
            if (isHomeToolbarVisibleFlow) {
                HomeBottomBar(homeNavigationController, currentDestination)
            }
        },
        floatingActionButton = {
            if (isHomeToolbarVisibleFlow) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 16.dp),
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    onClick = {
                        homeNavigationController.navigate(HomeNavigationDestination.ContactsDest.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Start a new conversation"
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            navController = homeNavigationController,
            startDestination = HomeNavigationDestination.ConversationDest.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            composable(
                route = HomeNavigationDestination.ConversationDest.route,
            ) {
                val conversationViewmodel: ConversationViewmodel = hiltViewModel()
                LaunchedEffect(homeViewmodel) {
                    homeViewmodel.updateHomeToolbarState(true)
                }
                ConversationsScreen(
                    homeNavigationController = homeNavigationController,
                    homeViewmodel = homeViewmodel,
                    conversationViewmodel = conversationViewmodel,
                    appLevelNavController = appLevelNavController
                )
            }
            composable(
                route = HomeNavigationDestination.ContactsDest.route,
            ) {
                val contactViewmodel: ContactViewmodel = hiltViewModel()
                LaunchedEffect(homeViewmodel) {
                    homeViewmodel.updateHomeToolbarState(false)
                }
                ContactScreen(
                    homeNavController = homeNavigationController,
                    homeViewmodel = homeViewmodel,
                    contactViewmodel = contactViewmodel,
                    appLevelNavController = appLevelNavController
                )
            }
            composable(
                route = HomeNavigationDestination.Call.route,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Call Screen")
                }
            }

            composable(
                route = HomeNavigationDestination.Settings.route,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Settings Screen")
                }
            }
        }
    }
}



package com.devvikram.talkzy.ui.screens.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.devvikram.talkzy.ui.screens.HomeViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    homeNavController: NavController,
    homeViewmodel: HomeViewmodel,
    contactViewmodel: ContactViewmodel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            homeNavController.popBackStack()
                        }
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it)
        ){
            Text("Contacts")
        }
    }

}
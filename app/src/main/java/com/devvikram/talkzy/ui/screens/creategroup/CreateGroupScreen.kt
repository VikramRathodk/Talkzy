package com.devvikram.talkzy.ui.screens.creategroup

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.ui.dialogs.CreateGroupDialog
import com.devvikram.talkzy.ui.navigation.HomeNavigationDestination
import com.devvikram.talkzy.ui.screens.contacts.ContactItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    appViewmodel: AppViewModel,
    appLevelNavController: NavController,
    createGroupViewModel: CreateGroupViewModel = hiltViewModel()
) {
    val contacts = createGroupViewModel.contactList.collectAsState(initial = emptyList())
    val selectedContacts =
        createGroupViewModel.selectedContacts.collectAsState(initial = emptyList())
    val isCreateGroupDialogOpened = createGroupViewModel.isCreateGroupDialogOpened.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Group")
                },
                navigationIcon = {
                    IconButton(onClick = { appLevelNavController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                modifier = Modifier.padding(8.dp),
                visible = selectedContacts.value.isNotEmpty(),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        createGroupViewModel.updateIsCreateGroupDialogOpened(true)
                    }
                ) {
                    Text(text = "Create Group")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            items(contacts.value) { contact ->
                Log.d(TAG, "group contact : $contact")
                ContactItem(
                    isSelected = selectedContacts.value.contains(contact),
                    contact = contact,
                ) {
                    createGroupViewModel.toggleContactSelection(contact)
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        }
    }

    // create group dialog
    if (isCreateGroupDialogOpened.value) {
        CreateGroupDialog(
            selectedContacts = selectedContacts.value,
            onDismiss = {
                createGroupViewModel.updateIsCreateGroupDialogOpened(false)
            },
            onCreateGroup = { groupName, groupDescription ->
                Log.d(TAG, "onCreateGroup: $groupName, $groupDescription")
                createGroupViewModel.createAGroup(
                    groupName,
                    groupDescription,
                    selectedContacts.value,
                )
            }
        )
    }

}
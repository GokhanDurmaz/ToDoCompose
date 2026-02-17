package com.flowintent.workspace.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.workspace.ui.vm.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoNavTopBar(
    state: TopBarState,
    onSearchToggle: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedCount = viewModel.selectedCount
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isSelectionMode) "$selectedCount Selected" else state.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1
                    )
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            viewModel.setSelectionMode(false)
                            viewModel.unselectAll()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    } else if (state.showProfileIcon) {
                        ProfileIcon()
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            viewModel.deleteSelectedTasks()
                            viewModel.setSelectionMode(false)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    } else if (state.showMenu) {
                        TopBarMenu(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            isSearchBarVisible = state.isSearchBarVisible,
                            onSearchToggle = onSearchToggle
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = bottomBar
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
private fun TopBarMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    isSearchBarVisible: Boolean,
    onSearchToggle: () -> Unit
) {
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(onClick = { onExpandedChange(true) }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Open Menu")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            DropdownMenuItem(
                text = { Text(if (isSearchBarVisible) "Close Search" else "Search") },
                onClick = {
                    onSearchToggle()
                    onExpandedChange(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = if (isSearchBarVisible) Icons.Default.SearchOff else Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun ProfileIcon() {
    IconButton(onClick = { }) {
        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
    }
}

data class TopBarState(
    val title: String = "My Tasks",
    val showProfileIcon: Boolean = true,
    val showMenu: Boolean = true,
    val isSearchBarVisible: Boolean = false
)

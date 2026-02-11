package com.flowintent.workspace.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.workspace.ui.dialog.TaskDialogHandler
import com.flowintent.workspace.ui.vm.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoNavTopBar(
    viewModel: TaskViewModel = hiltViewModel(),
    scope: @Composable (PaddingValues) -> Unit
) {
    var isShowing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = "To-Do List") },
                windowInsets = WindowInsets(0, 0, 0, 0),
                navigationIcon = { ProfileIcon() },
                actions = {
                    NavActions(
                        viewModel = viewModel,
                        onAddTaskClick = { isShowing = true }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        scope(paddingValues)
    }

    TaskDialogHandler(
        isShowing = isShowing,
        isUpdate = false,
        viewModel = viewModel,
        onDismiss = { isShowing = false }
    )
}

@Composable
private fun ProfileIcon() {
    IconButton(onClick = { }) {
        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile")
    }
}

@Composable
private fun NavActions(
    viewModel: TaskViewModel,
    onAddTaskClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Task")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Add Task") },
                onClick = {
                    expanded = false
                    onAddTaskClick()
                }
            )
            SelectionMenuItems(viewModel, isSelectionMode) { expanded = false }
        }
    }
}

@Composable
private fun ColumnScope.SelectionMenuItems(
    viewModel: TaskViewModel,
    isSelectionMode: Boolean,
    onFinish: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(if (isSelectionMode) "Unselect All" else "Select All") },
        onClick = {
            if (isSelectionMode) {
                viewModel.unselectAll()
                viewModel.setSelectionMode(false)
            } else {
                viewModel.setSelectionMode(true)
                viewModel.selectAll()
            }
            onFinish()
        }
    )
    DropdownMenuItem(
        text = { Text("Delete Selected") },
        onClick = {
            viewModel.deleteSelectedTasks()
            viewModel.setSelectionMode(false)
            onFinish()
        }
    )
}

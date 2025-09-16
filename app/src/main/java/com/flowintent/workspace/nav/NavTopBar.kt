package com.flowintent.workspace.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import com.flowintent.workspace.ui.vm.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoNavTopBar(scope: @Composable (PaddingValues) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var isShowing: Boolean by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = "To-Do List")
                },
                windowInsets = WindowInsets(0,0,0,0),
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {

                    }) {
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add New Task"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Add Task") },
                                    onClick = {
                                        expanded = false
                                        isShowing = !isShowing
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Select Task") },
                                    onClick = {
                                        expanded = false
                                        // TODO: Select task action
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        scope.invoke(paddingValues)
    }
    if (isShowing) {
        OpenTaskDialog(
            onDismiss = {
                isShowing = !isShowing
            }
        )
    }
}

@Composable
fun OpenTaskDialog(
    viewModel: TaskViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    isUpdate: Boolean = false
) {
    val toDoLabel = remember { mutableStateOf("") }
    val toDoContent = remember { mutableStateOf("") }
    val updateTaskId by viewModel.updateTaskId.collectAsStateWithLifecycle()

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column {
                OutlinedTextField(
                    value = toDoLabel.value,
                    onValueChange = {
                        toDoLabel.value = it
                    },
                    placeholder = { Text(text = "label") },
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp, top = 12.dp, end = 12.dp),
                    label = { Text(text = "Label") }
                )
                OutlinedTextField(
                    value = toDoContent.value,
                    onValueChange = {
                        toDoContent.value = it
                    },
                    placeholder = { Text(text = "describe todo content here.") },
                    modifier = Modifier.fillMaxWidth()
                        .height(height = 100.dp)
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp),
                    maxLines = 4
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        content = {
                            Text(text = "CANCEL")
                        },
                        modifier = Modifier.padding(start = 12.dp, top = 24.dp)
                    )
                    Button(
                        onClick = {
                            if (toDoContent.value.isEmpty() or toDoLabel.value.isEmpty()) {
                                onDismiss()
                            }
                            if (isUpdate.not()) {
                                viewModel.insertTask(
                                    Task(
                                        title = toDoLabel.value,
                                        content = TaskRes.TaskContent(toDoContent.value),
                                        taskType = TaskType.LOCAL_TASKS,
                                        cardColor = 0xFF6200EE.toInt(),
                                        iconColor = 0xFFFFFFFF.toInt(),
                                        textColor = 0xFF000000.toInt()
                                    )
                                )
                            } else {
                                viewModel.updateTask(
                                    id = updateTaskId ?: -1,
                                    title = toDoLabel.value,
                                    content = TaskRes.TaskContent(toDoContent.value)
                                )
                            }
                            onDismiss()
                        },
                        content = {
                            Text(text = "OK")
                        },
                        modifier = Modifier.padding(top = 24.dp, end = 12.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}

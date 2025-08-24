package com.flowintent.workspace.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.flowintent.core.db.room.Task
import com.flowintent.core.db.room.TaskRes
import com.flowintent.workspace.data.local.LocalTaskDataProvider
import com.flowintent.workspace.ui.vm.TaskViewModel

@Composable
fun ToDoHomeScreen(modifier: Modifier) {
    ToDoAppContent()
}

@Composable
private fun ToDoAppContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddTaskButton()
    }
}

@Composable
private fun AddTaskButton() {
    var isShowing: Boolean by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.padding(start = 12.dp, top = 24.dp, end = 12.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .weight(1f)
                .padding(end = 6.dp)
                .height(height = 100.dp),
            onClick = {
                Log.d("AddTaskButton", "onClick -- AddTaskButton")
                isShowing = !isShowing
            },
            content = {
                Text(text = "New Task", fontSize = 16.sp)
            },
            shape = RoundedCornerShape(6.dp)
        )
        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .weight(1f)
                .padding(start = 6.dp)
                .height(height = 100.dp),
            onClick = {

            },
            content = {
                Text(text = "Add Quick Task", fontSize = 16.sp)
            },
            shape = RoundedCornerShape(6.dp)
        )
    }
    Row(
        modifier = Modifier.padding(start = 12.dp, top = 6.dp, end = 12.dp, bottom = 12. dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .weight(1f)
                .padding(end = 6.dp)
                .height(height = 100.dp),
            onClick = {

            },
            content = {
                Text(text = "Add Urgent Task", fontSize = 16.sp)
            },
            shape = RoundedCornerShape(6.dp)
        )
        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .weight(1f)
                .padding(start = 6.dp)
                .height(height = 100.dp),
            onClick = {

            },
            content = {
                Text(text = "Add Reminder", fontSize = 16.sp)
            },
            shape = RoundedCornerShape(6.dp)
        )
    }
    if (isShowing) {
        AddTaskDialog(
            onDismiss = {
                isShowing = !isShowing
            }
        )
    }
}

@Composable
private fun AddTaskDialog(
    viewModel: TaskViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val toDoLabel = remember { mutableStateOf("") }
    val toDoContent = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column {
                OutlinedTextField(
                    value = toDoLabel.value,
                    onValueChange = {
                        toDoLabel.value = it
                    },
                    placeholder = { Text(text = "label") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp),
                    label = { Text(text = "label") }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp)
                ) {
                    Button(
                        onClick = { expanded = !expanded },
                        content = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Choose Task Privilege")
                                Icon(Icons.Default.Check, contentDescription = "More options")
                            }
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth(),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Primary") },
                            onClick = {  }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Secondary") },
                            onClick = {  }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Less Important") },
                            onClick = {  }
                        )
                    }
                }
                OutlinedTextField(
                    value = toDoContent.value,
                    onValueChange = {
                        toDoContent.value = it
                    },
                    placeholder = { Text(text = "describe todo content here.") },
                    modifier = Modifier
                        .fillMaxWidth()
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
                            if (toDoContent.value.isEmpty().not()
                                and toDoLabel.value.isEmpty().not()) {
                                viewModel.insertTask(
                                    Task(
                                        name = toDoLabel.value,
                                        content = TaskRes.TaskContent(toDoContent.value),
                                        taskType = LocalTaskDataProvider.defaultTask.taskType,
                                    )
                                )
                                Toast.makeText(context, "Created a task.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Please do not type empty text.", Toast.LENGTH_SHORT).show()
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

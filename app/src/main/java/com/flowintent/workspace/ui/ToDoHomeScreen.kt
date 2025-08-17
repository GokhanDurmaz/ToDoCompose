package com.flowintent.workspace.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.flowintent.workspace.data.TaskRes
import com.flowintent.workspace.data.local.LocalTaskDataProvider
import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.ui.vm.TaskViewModel

@Composable
fun ToDoHomeScreen(
    viewModel: TaskViewModel,
    modifier: Modifier
) {
    ToDoAppContent(viewModel)
}

@Composable
private fun ToDoAppContent(
    viewModel: TaskViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AddTaskButton(viewModel)
        TaskButton()
    }
}

@Composable
private fun AddTaskButton(viewModel: TaskViewModel) {
    var isShowing: Boolean by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            onClick = {
                Log.d("AddTaskButton", "onClick -- AddTaskButton")
                isShowing = !isShowing
            },
            content = {
                Text(text = "Add New Task")
            }
        )
    }
    if (isShowing) {
        AddTaskDialog(
            viewModel = viewModel,
            onDismiss = {
                isShowing = !isShowing
            }
        )
    }
}

@Composable
private fun AddTaskDialog(
    viewModel: TaskViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val toDoLabel = remember { mutableStateOf("") }
    val toDoContent = remember { mutableStateOf("") }

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
                    label = { Text(text = "label") }
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

// It will be expandable view when touching the task view.
@Composable
private fun TaskButton() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            colors = ButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.White,
            ),
            onClick = {

            },
            content = {
                Text(text = "Add New Task")
            }
        )
    }
}

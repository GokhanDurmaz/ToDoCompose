package com.flowintent.workspace.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.util.ColorPicker
import com.flowintent.workspace.util.ColorProvider
import com.flowintent.workspace.util.toArgbCompat

@Composable
fun OpenTaskDialog(
    viewModel: TaskViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    isUpdate: Boolean = false
) {
    val toDoLabel = remember { mutableStateOf("") }
    val toDoContent = remember { mutableStateOf("") }
    val updateTaskId by viewModel.updateTaskId.collectAsStateWithLifecycle()

    val colorPicker = remember { ColorPicker(ColorProvider.getShuffledColors()) }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column {
                TaskTextField(
                    valueState = toDoLabel.value,
                    onValueChange = { toDoLabel.value = it },
                    label = "Label",
                    placeholder = "Enter task label"
                )
                TaskTextField(
                    valueState = toDoContent.value,
                    onValueChange = { toDoContent.value = it },
                    label = "Description",
                    placeholder = "Describe todo content here",
                    maxLines = 4,
                    height = 100.dp
                )
                DialogButtons(
                    modifier = Modifier.padding(12.dp),
                    onDismiss = onDismiss,
                    onConfirm = {
                        if (toDoContent.value.isEmpty() or toDoLabel.value.isEmpty()) {
                            onDismiss()
                        }
                        if (isUpdate.not()) {
                            viewModel.insertTask(
                                Task(
                                    title = toDoLabel.value,
                                    content = TaskRes.TaskContent(toDoContent.value),
                                    taskType = TaskType.LOCAL_TASKS,
                                    cardColor = colorPicker.next().toArgbCompat(),
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
                    }
                )
            }
        }
    }
}

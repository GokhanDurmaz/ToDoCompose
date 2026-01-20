package com.flowintent.workspace.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
                TaskTextFields(toDoLabel, toDoContent)

                DialogButtons(
                    modifier = Modifier.padding(12.dp),
                    onDismiss = onDismiss,
                    onConfirm = {
                        val input = TaskInput(
                            title = toDoLabel.value,
                            content = toDoContent.value,
                            isUpdate = isUpdate,
                            updateTaskId = updateTaskId,
                            colorPicker = colorPicker
                        )
                        viewModel.handleTaskConfirm(input, onDismiss)
                    }
                )
            }
        }
    }
}

@Composable
private fun TaskTextFields(toDoLabel: MutableState<String>, toDoContent: MutableState<String>) {
    TaskTextField(
        valueState = toDoLabel.value,
        onValueChange = { toDoLabel.value = it },
        label = "Label",
        placeholder = "Enter task label",
        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, top = 12.dp, end = 12.dp)
    )
    TaskTextField(
        valueState = toDoContent.value,
        onValueChange = { toDoContent.value = it },
        label = "Description",
        placeholder = "Describe todo content here",
        maxLines = 4,
        modifier = Modifier.fillMaxWidth().height(100.dp).padding(start = 12.dp, top = 12.dp, end = 12.dp)
    )
}

private fun TaskViewModel.handleTaskConfirm(
    input: TaskInput,
    onDismiss: () -> Unit
) {
    if (input.content.isEmpty() || input.title.isEmpty()) {
        onDismiss()
        return
    }

    if (!input.isUpdate) {
        this.insertTask(
            Task(
                title = input.title,
                content = TaskRes.TaskContent(input.content),
                taskType = TaskType.LOCAL_TASKS,
                cardColor = input.colorPicker.next().toArgbCompat(),
                iconColor = 0xFFFFFFFF.toInt(),
                textColor = 0xFF000000.toInt(),
                dueDate = System.currentTimeMillis()
            )
        )
    } else {
        this.updateTask(
            id = input.updateTaskId ?: -1,
            title = input.title,
            content = TaskRes.TaskContent(input.content)
        )
    }
    onDismiss()
}

data class TaskInput(
    val title: String,
    val content: String,
    val isUpdate: Boolean,
    val updateTaskId: Int?,
    val colorPicker: ColorPicker
)

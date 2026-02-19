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
    val commonModifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp)
    val labelConfig = TaskTextFieldConfig("Label", "Enter task label")
    val contentConfig = TaskTextFieldConfig("Description", "Describe todo content here", maxLines = 4)

    Column {
        TaskTextField(
            value = toDoLabel.value,
            onValueChange = { toDoLabel.value = it },
            config = labelConfig,
            modifier = commonModifier
        )
        TaskTextField(
            value = toDoContent.value,
            onValueChange = { toDoContent.value = it },
            config = contentConfig,
            modifier = commonModifier.height(100.dp)
        )
    }
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
                taskType = TaskType.OTHER,
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

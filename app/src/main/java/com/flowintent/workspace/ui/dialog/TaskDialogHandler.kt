package com.flowintent.workspace.ui.dialog

import androidx.compose.runtime.Composable
import com.flowintent.workspace.ui.vm.TaskViewModel

@Composable
fun TaskDialogHandler(
    isShowing: Boolean,
    isUpdate: Boolean,
    viewModel: TaskViewModel,
    onDismiss: () -> Unit
) {
    if (isShowing) {
        OpenTaskDialog(
            viewModel = viewModel,
            isUpdate = isUpdate,
            onDismiss = onDismiss
        )
    }
}

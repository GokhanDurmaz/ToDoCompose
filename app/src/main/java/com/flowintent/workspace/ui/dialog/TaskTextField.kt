package com.flowintent.workspace.ui.dialog

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TaskTextField(
    value: String,
    onValueChange: (String) -> Unit,
    config: TaskTextFieldConfig,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(config.label) },
        placeholder = { Text(config.placeholder) },
        modifier = modifier,
        maxLines = config.maxLines
    )
}

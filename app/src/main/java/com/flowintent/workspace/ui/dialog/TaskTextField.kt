package com.flowintent.workspace.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TaskTextField(
    valueState: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    height: Dp? = null,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = valueState,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (height != null) Modifier.height(height) else Modifier)
            .padding(start = 12.dp, top = 12.dp, end = 12.dp),
        maxLines = maxLines
    )
}

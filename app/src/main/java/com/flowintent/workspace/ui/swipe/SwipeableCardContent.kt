package com.flowintent.workspace.ui.swipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flowintent.core.db.Task
import com.flowintent.workspace.ui.button.CustomRadioButton
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.util.getRelativeDayLabel

@Composable
fun SwipeableCardContent(
    task: Task,
    content: @Composable () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val isSelected = viewModel.selectedTasks[task.uid] ?: false

    Box(modifier = Modifier.fillMaxSize()) {
        // UID bar
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)
                .align(Alignment.CenterStart),
            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(task.cardColor)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(task.uid.toString(), color = Color.White, textAlign = TextAlign.Center)
            }
        }

        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomRadioButton(
                    selected = isSelected,
                    onClick = { viewModel.toggleSelection(task.uid) },
                    modifier = Modifier.padding(start = 8.dp)
                )
                content()
            }
            Text(
                text = getRelativeDayLabel(task.dueDate),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

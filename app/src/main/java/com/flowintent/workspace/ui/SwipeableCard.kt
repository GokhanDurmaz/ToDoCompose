package com.flowintent.workspace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.Task
import com.flowintent.workspace.nav.OpenTaskDialog
import com.flowintent.workspace.ui.vm.TaskViewModel
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    task: Task,
    viewModel: TaskViewModel,
    content: @Composable () -> Unit
) {
    var isShowing: Boolean by remember { mutableStateOf(false) }
    val deleteResult by viewModel.deleteResult.collectAsStateWithLifecycle()
    val maxSwipe = with(LocalDensity.current) { 200.dp.toPx() }
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(80.dp)
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(200.dp)
                .background(Color.Red),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                viewModel.deleteTask(task)
                deleteResult
            }) {
                Text("Delete", color = Color.White)
            }
            TextButton(onClick = {
                viewModel.setUpdateTaskId(task.uid)
                isShowing = !isShowing
            }) {
                Text("Edit", color = Color.White)
            }
        }

        if (isShowing) {
            OpenTaskDialog(
                viewModel = viewModel,
                isUpdate = true,
                onDismiss = {
                    isShowing = !isShowing
                }
            )
        }

        Card(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newValue = offsetX + delta
                        offsetX = newValue.coerceIn(-maxSwipe, 0f)
                    },
                    onDragStopped = {
                        offsetX = if (offsetX < -maxSwipe / 2) -maxSwipe else 0f
                    }
                )
                .fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                content()
            }
        }
    }
}

package com.flowintent.workspace.ui.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flowintent.core.db.Task
import com.flowintent.workspace.ui.dialog.TaskDialogHandler
import com.flowintent.workspace.ui.vm.TaskViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    task: Task,
    viewModel: TaskViewModel = hiltViewModel(),
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onHeightChange: (Dp) -> Unit,
    content: @Composable () -> Unit
) {
    val isExpanded = viewModel.expandedMap[task.uid] ?: false
    var isShowing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val maxSwipe = with(LocalDensity.current) { 200.dp.toPx() }

    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) 100.dp else 50.dp,
        animationSpec = tween(300),
        label = "cardHeight"
    )

    LaunchedEffect(cardHeight) { onHeightChange(cardHeight) }

    val draggableState = rememberDraggableState { delta ->
        scope.launch { offsetX.snapTo((offsetX.value + delta).coerceIn(-maxSwipe, 0f)) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .background(Color.Transparent)
    ) {
        SwipeActions(
            modifier = Modifier.align(Alignment.CenterEnd),
            onDelete = onDelete,
            onEdit = {
                onEdit()
                isShowing = true
            },
            scope = scope,
            offsetX = offsetX,
            maxSwipe = maxSwipe
        )

        TaskDialogHandler(
            isShowing = isShowing,
            isUpdate = true,
            viewModel = viewModel,
            onDismiss = {
                isShowing = false
                scope.launch { offsetX.animateTo(0f, tween(300)) }
            }
        )

        Card(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .clickable { viewModel.toggleExpanded(task.uid) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        scope.launch {
                            val target = if (offsetX.value < -maxSwipe / 2) -maxSwipe else 0f
                            offsetX.animateTo(target, tween(300))
                        }
                    }
                )
                .fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            SwipeableCardContent(task = task, content = content)
        }
    }
}

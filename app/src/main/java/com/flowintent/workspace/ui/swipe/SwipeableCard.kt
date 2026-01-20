package com.flowintent.workspace.ui.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flowintent.core.db.Task
import com.flowintent.workspace.ui.dialog.TaskDialogHandler
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.util.VAL_0
import com.flowintent.workspace.util.VAL_0_0
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_2
import com.flowintent.workspace.util.VAL_200
import com.flowintent.workspace.util.VAL_300
import com.flowintent.workspace.util.VAL_4
import com.flowintent.workspace.util.VAL_50
import com.flowintent.workspace.util.VAL_80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
@Composable
fun SwipeableCard(
    task: Task,
    actions: SwipeActionCallbacks,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val isExpanded = viewModel.expandedMap[task.uid] ?: false
    var isShowing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(VAL_0_0) }
    val maxSwipe = with(LocalDensity.current) { VAL_200.dp.toPx() }

    val stateHolder = remember(offsetX, maxSwipe, scope) {
        SwipeStateHolder(offsetX, maxSwipe, scope)
    }

    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) VAL_80.dp else VAL_50.dp,
        animationSpec = tween(VAL_300), label = "cardHeight"
    )
    LaunchedEffect(cardHeight) { actions.onHeightChange(cardHeight) }

    Box(
        modifier = modifier.fillMaxWidth().height(cardHeight).background(Color.Transparent)
    ) {
        SwipeActions(
            modifier = Modifier.align(Alignment.CenterEnd),
            stateHolder = stateHolder,
            onDelete = actions.onDelete,
            onEdit = { actions.onEdit(); isShowing = true }
        )

        TaskDialogHandler(
            isShowing = isShowing,
            isUpdate = true,
            viewModel = viewModel,
            onDismiss = {
                isShowing = false
                scope.launch { offsetX.animateTo(VAL_0_0, tween(VAL_300)) }
            }
        )

        val swipeState = rememberSwipeState(offsetX, maxSwipe, scope)

        MainCardContent(task, viewModel, swipeState, content)
    }
}

@Composable
private fun rememberSwipeState(
    offsetX: Animatable<Float, AnimationVector1D>,
    maxSwipe: Float,
    scope: CoroutineScope
): SwipeState = remember(offsetX.value) {
    SwipeState(
        offsetX = offsetX.value,
        onDrag = { delta ->
            scope.launch { offsetX.snapTo((offsetX.value + delta).coerceIn(-maxSwipe, VAL_0_0)) }
        },
        onDragStopped = {
            scope.launch {
                val target = if (offsetX.value < -maxSwipe / VAL_2) -maxSwipe else VAL_0_0
                offsetX.animateTo(target, tween(VAL_300))
            }
        }
    )
}

data class SwipeState(
    val offsetX: Float,
    val onDrag: (Float) -> Unit,
    val onDragStopped: () -> Unit
)

@Composable
private fun MainCardContent(
    task: Task,
    viewModel: TaskViewModel,
    swipeState: SwipeState,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .offset { IntOffset(swipeState.offsetX.roundToInt(), VAL_0) }
            .clickable { viewModel.toggleExpanded(task.uid) }
            .draggable(
                state = rememberDraggableState { delta -> swipeState.onDrag(delta) },
                orientation = Orientation.Horizontal,
                onDragStopped = { swipeState.onDragStopped() }
            )
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = VAL_4.dp),
        shape = RoundedCornerShape(VAL_12.dp)
    ) {
        SwipeableCardContent(task = task, content = content)
    }
}

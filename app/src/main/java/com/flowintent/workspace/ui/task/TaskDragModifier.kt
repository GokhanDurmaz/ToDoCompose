package com.flowintent.workspace.ui.task

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.flowintent.core.db.model.DragInfo
import com.flowintent.core.db.model.calculateNewIndex
import com.flowintent.core.db.model.swap
import com.flowintent.uikit.util.VAL_0_0
import com.flowintent.uikit.util.VAL_12_0
import com.flowintent.uikit.util.VAL_2_0

fun Modifier.taskDragModifier(
    dragState: DragState,
    onDragUpdate: (DragInfo?) -> Unit,
    onLongPress: () -> Unit
): Modifier = this.then(
    Modifier
        .zIndex(if (dragState.isDragging) 1f else 0f)
        .graphicsLayer {
            if (dragState.isDragging) {
                val heightPx = dragState.itemHeight.toPx()
                translationY = dragState.draggingItem?.let { drag ->
                    drag.offsetY - drag.touchOffset + heightPx / VAL_2_0
                } ?: VAL_0_0

                shadowElevation = 16.dp.toPx()
                cameraDistance = VAL_12_0 * density
            }
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    onLongPress()
                    onDragUpdate(
                        DragInfo(
                            index = dragState.index,
                            offsetY = VAL_0_0,
                            touchOffset = offset.y
                        )
                    )
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    val drag = dragState.draggingItem ?: return@detectDragGesturesAfterLongPress
                    val updatedDrag = drag.copy(offsetY = drag.offsetY + dragAmount.y)
                    onDragUpdate(updatedDrag)

                    val newIndex = calculateNewIndex(
                        updatedDrag,
                        dragState.filteredList.size,
                        dragState.itemHeight.toPx()
                    )
                    if (newIndex != updatedDrag.index) {
                        dragState.filteredList.swap(updatedDrag.index, newIndex)
                        onDragUpdate(updatedDrag.copy(index = newIndex, offsetY = 0f))
                    }
                },
                onDragEnd = { onDragUpdate(null) },
                onDragCancel = { onDragUpdate(null) }
            )
        }
)

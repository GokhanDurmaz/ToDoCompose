package com.flowintent.workspace.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Doorbell
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.DragInfo
import com.flowintent.core.db.Task
import com.flowintent.core.db.calculateNewIndex
import com.flowintent.core.db.swap
import com.flowintent.workspace.nav.ToDoNavTopBar
import com.flowintent.workspace.ui.search.SearchBar
import com.flowintent.workspace.ui.swipe.SwipeableCard
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.util.VAL_0_0
import com.flowintent.workspace.util.VAL_0_7
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_16
import com.flowintent.workspace.util.VAL_1_0
import com.flowintent.workspace.util.VAL_1_0_2
import com.flowintent.workspace.util.VAL_2_0
import com.flowintent.workspace.util.VAL_50
import com.flowintent.workspace.util.asString

@Composable
fun ToDoListScreen() {
    val focusManager = LocalFocusManager.current
    var isSearchBarVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        ToDoNavTopBar { paddingValues ->
            Column {
                ListActionBar(
                    paddingTopOffset = paddingValues,
                    onSearchIconClick = { isSearchBarVisible = !isSearchBarVisible}
                )
                ListCardContent(
                    isSearchBarVisible = isSearchBarVisible,
                    focusManager = focusManager
                )
            }
        }
    }
}

@Composable
fun ListActionBar(
    paddingTopOffset: PaddingValues,
    onSearchIconClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingTopOffset)
            .padding(bottom = VAL_12.dp),
        shape = RectangleShape
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(VAL_16.dp)
                    .weight(VAL_0_7),
                text = "To-Do List",
                fontWeight = FontWeight.Bold
            )
            // İkon grubunu dışarı aldık
            ActionBarIcons(onSearchIconClick = onSearchIconClick)
        }
    }
}

@Composable
private fun RowScope.ActionBarIcons(onSearchIconClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .weight(VAL_1_0)
    ) {
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Default.Category, contentDescription = "Category")
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Label, contentDescription = "Labels")
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Default.DashboardCustomize, contentDescription = "Customize")
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Default.Doorbell, contentDescription = "Notifications")
        }
        IconButton(onClick = onSearchIconClick) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
    }
}

@Composable
private fun ListCardContent(
    viewModel: TaskViewModel = hiltViewModel(),
    isSearchBarVisible: Boolean = false,
    focusManager: FocusManager
) {
    val taskList by viewModel.tasks.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    // Arama mantığını ayırdık
    val filteredList = remember(taskList, searchText) {
        if (searchText.isBlank()) taskList.toMutableStateList()
        else taskList.filter { it.title.contains(searchText, ignoreCase = true) }.toMutableStateList()
    }

    Column {
        if (isSearchBarVisible) {
            TaskSearchBar(searchText) { searchText = it }
        }

        TaskLazyList(
            filteredList = filteredList,
            viewModel = viewModel,
            focusManager = focusManager
        )
    }
}

@Composable
private fun TaskSearchBar(query: String, onQueryChange: (String) -> Unit) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = VAL_12.dp, end = VAL_12.dp, bottom = VAL_12.dp)
            .height(VAL_50.dp)
    )
}

@Composable
private fun TaskLazyList(
    filteredList: SnapshotStateList<Task>,
    viewModel: TaskViewModel,
    focusManager: FocusManager
) {
    var draggingItem by remember { mutableStateOf<DragInfo?>(null) }
    var itemHeight by remember { mutableStateOf(VAL_50.dp) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(filteredList, key = { _, task -> task.uid }) { index, task ->
            TaskItemContainer(
                index = index,
                task = task,
                draggingItem = draggingItem,
                itemHeight = itemHeight,
                filteredList = filteredList,
                onDragUpdate = { draggingItem = it },
                onHeightChange = { itemHeight = it },
                viewModel = viewModel,
                focusManager = focusManager
            )
        }
    }
}

@Composable
private fun TaskItemContainer(
    index: Int,
    task: Task,
    draggingItem: DragInfo?,
    itemHeight: Dp,
    filteredList: SnapshotStateList<Task>,
    onDragUpdate: (DragInfo?) -> Unit,
    onHeightChange: (Dp) -> Unit,
    viewModel: TaskViewModel,
    focusManager: FocusManager
) {
    val isDragging = draggingItem?.index == index

    val dragState = DragState(
        index = index,
        isDragging = isDragging,
        draggingItem = draggingItem,
        itemHeight = itemHeight,
        filteredList = filteredList
    )

    Box(
        modifier = Modifier
            .taskDragModifier(dragState, onDragUpdate = onDragUpdate)
            .clickable { focusManager.clearFocus() }
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SwipeableCard(
                task = task,
                onDelete = { viewModel.deleteTask(task) },
                onEdit = { viewModel.setUpdateTaskId(task.uid) },
                onHeightChange = onHeightChange,
            ) {
                TaskCardTextContent(task, viewModel)
            }
        }
    }
}

@Composable
private fun TaskCardTextContent(task: Task, viewModel: TaskViewModel) {
    val isExpanded = viewModel.expandedMap[task.uid] ?: false
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Text(
            text = task.title,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
        if (isExpanded) {
            Text(
                text = task.content.asString(),
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun Modifier.taskDragModifier(
    dragState: DragState,
    onDragUpdate: (DragInfo?) -> Unit
): Modifier = this.then(
    Modifier
        .graphicsLayer {
            if (dragState.isDragging) {
                val heightPx = dragState.itemHeight.toPx()
                translationY = dragState.draggingItem?.let { drag ->
                    drag.offsetY - drag.touchOffset + heightPx / VAL_2_0
                } ?: VAL_0_0
                shadowElevation = VAL_16.dp.toPx()
                scaleX = VAL_1_0_2
                scaleY = VAL_1_0_2
            }
        }
        .zIndex(if (dragState.isDragging) VAL_1_0 else VAL_0_0)
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
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

data class DragState(
    val index: Int,
    val isDragging: Boolean,
    val draggingItem: DragInfo?,
    val itemHeight: Dp,
    val filteredList: SnapshotStateList<Task>
)

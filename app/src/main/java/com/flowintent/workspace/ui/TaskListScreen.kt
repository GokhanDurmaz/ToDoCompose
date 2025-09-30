package com.flowintent.workspace.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
            .padding(bottom = 12.dp),
        shape = RectangleShape
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(0.7f),
                text = "To-Do List",
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = "Profile"
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Label,
                        contentDescription = "Profile"
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.DashboardCustomize,
                        contentDescription = "Profile"
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Doorbell,
                        contentDescription = "Profile"
                    )
                }
                IconButton(onClick = {
                    onSearchIconClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Profile"
                    )
                }
            }
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
    var draggingItem by remember { mutableStateOf<DragInfo?>(null) }
    var itemHeight by remember { mutableStateOf(50.dp) }

    val filteredList = remember(taskList, searchText) {
        if (searchText.isBlank()) {
            taskList.toMutableStateList()
        } else {
            taskList.filter { it.title.contains(searchText, ignoreCase = true) }.toMutableStateList()
        }
    }

    Column {
        if (isSearchBarVisible) {
            SearchBar(
                query = searchText,
                onQueryChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(filteredList, key = { index: Int, task: Task -> task.uid }) { index, task ->
                val isDragging = draggingItem?.index == index
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            if (isDragging) {
                                val heightPx = itemHeight.toPx()
                                translationY = draggingItem?.let { drag ->
                                    drag.offsetY - drag.touchOffset + heightPx / 2f
                                } ?: 0f
                                shadowElevation = 16.dp.toPx()
                                scaleX = 1.02f
                                scaleY = 1.02f
                            }
                        }
                        .zIndex(if (isDragging) 1f else 0f)
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { offset ->
                                    draggingItem = DragInfo(
                                        index = index,
                                        offsetY = 0f,
                                        touchOffset = offset.y
                                    )
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val drag = draggingItem ?: return@detectDragGesturesAfterLongPress

                                    draggingItem = drag.copy(
                                        offsetY = drag.offsetY + dragAmount.y
                                    )

                                    val newIndex = calculateNewIndex(
                                        draggingItem!!,
                                        filteredList.size,
                                        itemHeight = itemHeight.toPx()
                                    )
                                    if (newIndex != draggingItem!!.index) {
                                        filteredList.swap(draggingItem!!.index, newIndex)
                                        draggingItem = draggingItem!!.copy(
                                            index = newIndex,
                                            offsetY = 0f
                                        )
                                    }
                                },
                                onDragEnd = { draggingItem = null },
                                onDragCancel = { draggingItem = null }
                            )
                        }
                        .clickable {
                            focusManager.clearFocus()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SwipeableCard(
                            task = task,
                            onDelete = { viewModel.deleteTask(task) },
                            onEdit = { viewModel.setUpdateTaskId(task.uid) },
                            onHeightChange = { itemHeight = it },
                        ) {
                            val isExpanded = viewModel.expandedMap[task.uid] ?: false
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
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
                    }
                }
            }
        }
    }
}

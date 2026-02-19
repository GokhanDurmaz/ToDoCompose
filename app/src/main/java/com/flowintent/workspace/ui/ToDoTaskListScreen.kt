package com.flowintent.workspace.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.DragInfo
import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import com.flowintent.core.db.calculateNewIndex
import com.flowintent.core.db.swap
import com.flowintent.core.util.Resource
import com.flowintent.workspace.R
import com.flowintent.workspace.nav.ToDoNavTopBar
import com.flowintent.workspace.nav.TopBarState
import com.flowintent.workspace.ui.search.SearchBar
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.util.VAL_0_0
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_16
import com.flowintent.workspace.util.VAL_1_0
import com.flowintent.workspace.util.VAL_1_0_2
import com.flowintent.workspace.util.VAL_2_0
import com.flowintent.workspace.util.VAL_50
import com.flowintent.workspace.util.asString

@Composable
fun TaskInputBar(
    onSendMessage: (String) -> Unit,
    onFileClick: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onFileClick) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = stringResource(R.string.attach_file_desc),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(stringResource(R.string.type_new_todo)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onSendMessage(text)
                        text = ""
                    }
                },
                enabled = text.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.send_desc),
                    tint = if (text.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

@Composable
fun ToDoListScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val focusManager = LocalFocusManager.current
    var isSearchBarVisible by remember { mutableStateOf(false) }
    val smartTaskState by viewModel.smartTaskState.collectAsStateWithLifecycle()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { println("Selected File URI: $it") }
    }

    ToDoNavTopBar(
        viewModel = viewModel,
        state = TopBarState(
            title = stringResource(R.string.task_details_title),
            showProfileIcon = false,
            showMenu = isSearchBarVisible,
            isSearchBarVisible = isSearchBarVisible
        ),
        onSearchToggle = { isSearchBarVisible = !isSearchBarVisible },
        bottomBar = {
            TaskInputBar(
                onSendMessage = { viewModel.insertSmartTask(it) },
                onFileClick = { filePickerLauncher.launch("*/*") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            ListCardContent(
                viewModel = viewModel,
                isSearchBarVisible = isSearchBarVisible
            )

            SmartTaskStateOverlay(
                state = smartTaskState,
                onClearState = { viewModel.clearSmartTaskState() }
            )
        }
    }
}

@Composable
private fun SmartTaskStateOverlay(
    state: Resource<Unit>?,
    onClearState: () -> Unit
) {
    when (state) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AiThinkingIndicator()
            }
        }
        is Resource.Error -> {
            LaunchedEffect(state.message) {
                println("Error: ${state.message}")
                onClearState()
            }
        }
        is Resource.Success -> {
            LaunchedEffect(Unit) { onClearState() }
        }
        null -> {}
    }
}

@Composable
private fun AiThinkingIndicator() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
            Text(stringResource(R.string.ai_thinking))
        }
    }
}

@Composable
fun ListCardContent(
    viewModel: TaskViewModel,
    isSearchBarVisible: Boolean
) {
    val taskList by viewModel.tasks.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    val filteredList = remember(taskList, searchText) {
        if (searchText.isBlank()) taskList
        else taskList.filter {
            val contentText = when(val c = it.content) {
                is TaskRes.TaskContent -> c.content
                is TaskRes.TaskContentRes -> ""
            }
            it.title.contains(searchText, ignoreCase = true) ||
                    contentText.contains(searchText, ignoreCase = true)
        }
    }

    val groupedTasks = remember(filteredList) {
        filteredList.groupBy { it.taskType }
    }

    Column {
        if (isSearchBarVisible) {
            TaskSearchBar(
                query = searchText,
                onQueryChange = { searchText = it }
            )
        }

        if (filteredList.isEmpty()) {
            EmptyTaskPlaceholder()
        } else {
            TaskLazyList(
                groupedTasks = groupedTasks,
                viewModel = viewModel
            )
        }
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

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun TaskLazyList(
    groupedTasks: Map<TaskType, List<Task>>,
    viewModel: TaskViewModel
) {
    var draggingItem by remember { mutableStateOf<DragInfo?>(null) }
    var itemHeight by remember { mutableStateOf(50.dp) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        groupedTasks.forEach { (type, tasks) ->
            stickyHeader {
                CategoryHeader(type)
            }

            itemsIndexed(tasks, key = { _, task -> task.uid }) { index, task ->
                val dragStateParams = TaskDragState(
                    index = index,
                    draggingItem = draggingItem,
                    itemHeight = itemHeight,
                    filteredList = remember { tasks.toMutableStateList() },
                    onDragUpdate = { draggingItem = it },
                    onHeightChange = { itemHeight = it }
                )

                TaskItemContainer(
                    task = task,
                    dragStateParams = dragStateParams,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CategoryHeader(type: TaskType) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = type.name,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
private fun TaskItemContainer(
    task: Task,
    dragStateParams: TaskDragState,
    viewModel: TaskViewModel,
) {
    val isDragging = dragStateParams.draggingItem == task
    val dragState = DragState(
        index = dragStateParams.index,
        isDragging = isDragging,
        draggingItem = dragStateParams.draggingItem,
        itemHeight = dragStateParams.itemHeight,
        filteredList = dragStateParams.filteredList
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .taskDragModifier(dragState, onDragUpdate = dragStateParams.onDragUpdate),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            TaskCardTextContent(task, viewModel)
        }
    }
}

@Composable
fun TaskCardTextContent(task: Task, viewModel: TaskViewModel) {
    val isExpanded = viewModel.expandedMap[task.uid] ?: false
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Text(
            text = task.content.asString(),
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

@Composable
fun EmptyTaskPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AttachFile,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_tasks_yet),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.empty_task_description),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun Modifier.taskDragModifier(
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

data class TaskDragState(
    val index: Int,
    val draggingItem: DragInfo?,
    val itemHeight: Dp,
    val filteredList: SnapshotStateList<Task>,
    val onDragUpdate: (DragInfo?) -> Unit,
    val onHeightChange: (Dp) -> Unit
)

data class DragState(
    val index: Int,
    val isDragging: Boolean,
    val draggingItem: DragInfo?,
    val itemHeight: Dp,
    val filteredList: SnapshotStateList<Task>
)

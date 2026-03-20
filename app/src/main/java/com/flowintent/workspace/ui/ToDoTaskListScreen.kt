package com.flowintent.workspace.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.toDisplayContent
import com.flowintent.core.util.Resource
import com.flowintent.core.util.toReadableDateTime
import com.flowintent.uikit.util.VAL_0_0
import com.flowintent.uikit.util.VAL_1_0
import com.flowintent.uikit.util.VAL_1_0_2
import com.flowintent.workspace.R
import com.flowintent.workspace.nav.route.ToDoNavTopBar
import com.flowintent.workspace.nav.route.TopBarState
import com.flowintent.workspace.ui.card.CategoryChipsRow
import com.flowintent.workspace.ui.task.AiThinkingIndicator
import com.flowintent.workspace.ui.task.EmptyTaskPlaceholder
import com.flowintent.workspace.ui.task.TaskSearchBar
import com.flowintent.workspace.ui.vm.TaskViewModel

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
fun ListCardContent(
    viewModel: TaskViewModel,
    isSearchBarVisible: Boolean
) {
    val pagingTasks = viewModel.tasks.collectAsLazyPagingItems()
    val searchText by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()

    Column {
        if (isSearchBarVisible) {
            TaskSearchBar(
                query = searchText,
                onQueryChange = { viewModel.onSearch(it) }
            )
        }

        CategoryChipsRow(
            selectedType = selectedType,
            onTypeSelected = { type ->
                viewModel.onTypeSelected(if (selectedType == type) null else type)
            }
        )

        if (pagingTasks.itemCount == 0) {
            EmptyTaskPlaceholder()
        } else {
            TaskLazyList(
                pagingTasks = pagingTasks,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskLazyList(
    pagingTasks: LazyPagingItems<Task>,
    viewModel: TaskViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = pagingTasks.itemCount,
            key = pagingTasks.itemKey { it.uid }
        ) { index ->
            val task = pagingTasks[index]
            if (task != null) {
                TaskItemContainer(
                    task = task,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun TaskItemContainer(
    task: Task,
    viewModel: TaskViewModel,
) {
    val haptic = LocalHapticFeedback.current
    val isSelected = viewModel.selectedTasks[task.uid] == true

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .pointerInput(task.uid) {
                detectTapGestures(
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleSelection(task.uid)
                    },
                    onTap = {
                        if (viewModel.selectedTasks.any { it.value }) {
                            viewModel.toggleSelection(task.uid)
                        } else {
                            viewModel.toggleExpanded(task.uid)
                        }
                    }
                )
            }
            .graphicsLayer {
                scaleX = if (isSelected) VAL_1_0_2 else VAL_1_0
                scaleY = if (isSelected) VAL_1_0_2 else VAL_1_0
            }
            .zIndex(if (isSelected) VAL_1_0 else VAL_0_0),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            TaskCardTextContent(task, viewModel)
        }
    }
}

@Composable
fun TaskCardTextContent(task: Task, viewModel: TaskViewModel) {
    val isExpanded = viewModel.expandedMap[task.uid] ?: false

    val displayText = remember(task.content) {
        task.content.toDisplayContent()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = task.dueDate.toReadableDateTime(),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        if (isExpanded) {
            Text(
                text = task.content.toDisplayContent(),
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

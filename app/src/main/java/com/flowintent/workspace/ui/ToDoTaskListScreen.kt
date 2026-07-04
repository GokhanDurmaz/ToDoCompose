 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.model.toDisplayContent
import com.flowintent.core.util.Resource
import com.flowintent.core.util.toReadableDateTime
import com.flowintent.uikit.util.VAL_0_0
import com.flowintent.uikit.util.VAL_1_0
import com.flowintent.uikit.util.VAL_1_0_2
import com.flowintent.workspace.R
import com.flowintent.workspace.nav.route.NavTopBarActions
import com.flowintent.workspace.nav.route.NavTopBarViewModels
import com.flowintent.workspace.nav.route.ToDoNavTopBar
import com.flowintent.workspace.nav.route.TopBarState
import com.flowintent.workspace.ui.card.CategoryChipsRow
import com.flowintent.workspace.ui.dialog.PermissionConsentDialog
import com.flowintent.workspace.ui.task.AiThinkingIndicator
import com.flowintent.workspace.ui.task.EmptyTaskPlaceholder
import com.flowintent.workspace.ui.task.TaskSearchBar
import com.flowintent.workspace.ui.vm.TaskViewModel

@Composable
fun TaskInputBar(
    onSendMessage: (String) -> Unit,
    onFileClick: () -> Unit,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    Surface(
        modifier = modifier.fillMaxWidth().padding(start = 24.dp, bottom = 90.dp, end = 24.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        tonalElevation = 4.dp
    ) {
        TaskInputRow(
            text = text,
            onTextChange = {
                text = it
                onValueChange(it)
            },
            onSendMessage = {
                onSendMessage(text)
                text = ""
            },
            onFileClick = onFileClick
        )
    }
}

@Composable
private fun TaskInputRow(
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onFileClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    stringResource(R.string.type_new_todo),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            maxLines = 4
        )
        IconButton(
            onClick = onSendMessage,
            enabled = text.isNotBlank()
        ) {
            val tint = if (text.isNotBlank()) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.send_desc),
                tint = tint
            )
        }
    }
}

data class TaskActions(
    val onSearch: (String) -> Unit,
    val onTypeSelected: (TaskType?) -> Unit,
    val onTaskLongPress: (Int) -> Unit,
    val onTaskTap: (Int) -> Unit
)

@Composable
fun ToDoListScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSearchBarVisible by remember { mutableStateOf(false) }
    val hasNotificationPermission = rememberPermissionState(context)

    NotificationPermissionHandler(
        showConsent = uiState.showPermissionConsent,
        hasPermission = hasNotificationPermission,
        onDismiss = viewModel::dismissPermissionConsent
    )

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { /* Handle file selection */ } }

    val actions = remember(viewModel) {
        TaskActions(
            onSearch = viewModel::onSearch,
            onTypeSelected = viewModel::onTypeSelected,
            onTaskLongPress = viewModel::toggleSelection,
            onTaskTap = { uid ->
                if (viewModel.uiState.value.selectedTasks.any { it.value }) {
                    viewModel.toggleSelection(uid)
                } else {
                    viewModel.toggleExpanded(uid)
                }
            }
        )
    }

    ToDoNavTopBar(
        state = TopBarState(
            title = stringResource(R.string.task_details_title),
            showProfileIcon = false,
            showMenu = isSearchBarVisible,
            isSearchBarVisible = isSearchBarVisible
        ),
        actions = NavTopBarActions(
            onSearchToggle = { isSearchBarVisible = !isSearchBarVisible }
        ),
        viewModels = NavTopBarViewModels(taskViewModel = viewModel, profileViewModel = hiltViewModel()),
        bottomBar = {
            TaskInputBar(
                onSendMessage = {
                    viewModel.insertSmartTask(it)
                    if (it.isNotEmpty() && !hasNotificationPermission) {
                        viewModel.onTaskInput()
                    }
                },
                onFileClick = { filePickerLauncher.launch("*/*") }
            )
        }
    ) { paddingValues ->
        ToDoListContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            uiState = uiState,
            isSearchBarVisible = isSearchBarVisible,
            actions = actions
        )
    }
}

@Composable
private fun rememberPermissionState(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

@Composable
private fun NotificationPermissionHandler(
    showConsent: Boolean,
    hasPermission: Boolean,
    onDismiss: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Handle result
    }

    if (showConsent && !hasPermission) {
        PermissionConsentDialog(
            onDismiss = onDismiss,
            onConfirm = {
                onDismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        )
    }
}

@Composable
private fun ToDoListContent(
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    viewModel: TaskViewModel,
    uiState: com.flowintent.workspace.ui.vm.TaskUiState,
    isSearchBarVisible: Boolean,
    actions: TaskActions
) {
    val focusManager = LocalFocusManager.current
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
            pagingTasks = viewModel.tasks.collectAsLazyPagingItems(),
            uiState = uiState,
            isSearchBarVisible = isSearchBarVisible,
            actions = actions
        )

        SmartTaskStateOverlay(
            state = uiState.smartTaskState,
            onClearState = { viewModel.clearSmartTaskState() }
        )
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
    pagingTasks: LazyPagingItems<Task>,
    uiState: com.flowintent.workspace.ui.vm.TaskUiState,
    isSearchBarVisible: Boolean,
    actions: TaskActions
) {
    Column {
        if (isSearchBarVisible) {
            TaskSearchBar(
                query = uiState.searchQuery,
                onQueryChange = actions.onSearch
            )
        }

        CategoryChipsRow(
            selectedType = uiState.selectedType,
            onTypeSelected = { type ->
                actions.onTypeSelected(if (uiState.selectedType == type) null else type)
            }
        )

        if (pagingTasks.itemCount == 0) {
            EmptyTaskPlaceholder()
        } else {
            TaskLazyList(
                pagingTasks = pagingTasks,
                selectedTasks = uiState.selectedTasks,
                expandedTasks = uiState.expandedTasks,
                onTaskLongPress = actions.onTaskLongPress,
                onTaskTap = actions.onTaskTap
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskLazyList(
    pagingTasks: LazyPagingItems<Task>,
    selectedTasks: Map<Int, Boolean>,
    expandedTasks: Map<Int, Boolean>,
    onTaskLongPress: (Int) -> Unit,
    onTaskTap: (Int) -> Unit
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
                    isSelected = selectedTasks[task.uid] ?: false,
                    isExpanded = expandedTasks[task.uid] ?: false,
                    onLongPress = { onTaskLongPress(task.uid) },
                    onTap = { onTaskTap(task.uid) }
                )
            }
        }
    }
}

@Composable
private fun TaskItemContainer(
    task: Task,
    isSelected: Boolean,
    isExpanded: Boolean,
    onLongPress: () -> Unit,
    onTap: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

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
                        onLongPress()
                    },
                    onTap = { onTap() }
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
            TaskCardTextContent(task, isExpanded)
        }
    }
}

@Composable
fun TaskCardTextContent(task: Task, isExpanded: Boolean) {
    val displayText = task.content.toDisplayContent()

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

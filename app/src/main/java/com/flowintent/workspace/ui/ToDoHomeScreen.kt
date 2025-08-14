package com.flowintent.workspace.ui

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.flowintent.workspace.R
import com.flowintent.workspace.data.ToDoUiState
import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.data.local.LocalTaskDataProvider
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.ui.vm.ToDoViewModel
import com.flowintent.workspace.util.MainContentType
import com.flowintent.workspace.util.MainNavigationType

@Composable
fun ToDoHomeScreen(
    viewModel: TaskViewModel,
    modifier: Modifier
) {

    val navigationItemContentList = listOf(
        NavigationItemContent(
            songType = TaskType.REMOTE_TASKS,
            icon = Icons.Default.Image,
            text = stringResource(R.string.default_task_content)
        )
    )

    ToDoAppContent(viewModel, modifier)
}

@Composable
private fun ToDoAppContent(
    viewModel: TaskViewModel,
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmptyTaskView()
        AddTaskButton(viewModel)
        TaskButton()
    }
}

@Composable
private fun EmptyTaskView() {
    Row {
        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(R.drawable.no_task),
                contentDescription = stringResource(R.string.empty_task),
                modifier = Modifier.size(90.dp)
            )
            Text(text = stringResource(R.string.empty_task))
        }
    }
}

@Composable
private fun AddTaskButton(viewModel: TaskViewModel) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            onClick = {
                Toast.makeText(context, "AddTaskButton", Toast.LENGTH_SHORT).show()
                viewModel.insertTask(
                    Task(
                        name = LocalTaskDataProvider.defaultTask.name,
                        content = LocalTaskDataProvider.defaultTask.content,
                        taskType = LocalTaskDataProvider.defaultTask.taskType,
                    )
                )
            },
            content = {
                Text(text = "Add New Task")
            }
        )
    }
}

// It will be expandable view when touching the task view.
@Composable
private fun TaskButton() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            colors = ButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.White,
            ),
            onClick = {

            },
            content = {
                Text(text = "Add New Task")
            }
        )
    }
}


@Composable
private fun MainAppContent(
    navigationType: MainNavigationType,
    contentType: MainContentType,
    songUiState: ToDoUiState,
    onTabPressed: (TaskType) -> Unit,
    onSongCardPressed: (Task) -> Unit,
    onDetailScreenBackPressed: () -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = true) {
                val navigationRailContentDescription = stringResource(R.string.default_task_content)
                MainNavigationRail(
                    currentTab = songUiState.taskListType,
                    onTabPressed = onTabPressed,
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier.testTag(navigationRailContentDescription)
                )
            }
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    selectedDestination: TaskType,
    onTabPressed: ((TaskType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        NavigationDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                selected = selectedDestination == navItem.songType,
                label = {
                    Text(
                        text = navItem.text,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { onTabPressed(navItem.songType) }
            )
        }
    }
}

@Composable
private fun NavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReplyLogo(modifier = Modifier.size(48.dp))
        ReplyProfileImage(
            drawableResource = R.drawable.logo,
            description = stringResource(id = R.string.profile),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ReplyProfileImage(
    @DrawableRes drawableResource: Int,
    description: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier.clip(CircleShape),
            painter = painterResource(drawableResource),
            contentDescription = description,
        )
    }
}

@Composable
fun ReplyLogo(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.logo),
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
}

@Composable
private fun MainNavigationRail(
    currentTab: TaskType,
    onTabPressed: (TaskType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier
) {
    NavigationRail(modifier = modifier) {
        navigationItemContentList.forEach {
            NavigationRailItem(
                selected = currentTab == it.songType,
                onClick = { onTabPressed(it.songType) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.text
                    )
                }
            )
        }
    }
}

private data class NavigationItemContent(
    val songType: TaskType,
    val icon: ImageVector,
    val text: String
)

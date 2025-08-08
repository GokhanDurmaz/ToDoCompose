package com.flowintent.workspace.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.flowintent.workspace.R
import com.flowintent.workspace.data.ToDoUiState
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.util.MainContentType
import com.flowintent.workspace.util.MainNavigationType

@Composable
fun MainHomeScreen(
    navigationType: MainNavigationType,
    contentType: MainContentType,
    songUiState: ToDoUiState,
    onTabPressed: (TaskType) -> Unit,
    onSongCardPressed: (Task) -> Unit,
    onDetailScreenBackPressed: () -> Unit,
    modifier: Modifier
) {

    val navigationItemContentList = listOf(
        NavigationItemContent(
            songType = TaskType.FOREIGN_SONGS,
            icon = Icons.Default.Image,
            text = stringResource(R.string.default_song_content)
        )
    )

    val navigationDrawerContentDescription+ = stringResource(R.string.default_song_content)
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(240.dp),
                drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ) {
                NavigationDrawerContent(
                    selectedDestination = songUiState.songListType,
                    onTabPressed = onTabPressed,
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(12.dp)
                )
            }
        },
        modifier = Modifier.testTag(navigationDrawerContentDescription)
    ) {
        MainAppContent(
            navigationType = navigationType,
            contentType = contentType,
            songUiState = songUiState,
            onTabPressed = onTabPressed,
            onSongCardPressed = onSongCardPressed,
            onDetailScreenBackPressed = onDetailScreenBackPressed,
            navigationItemContentList = navigationItemContentList,
            modifier = modifier,
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
                val navigationRailContentDescription = stringResource(R.string.default_song_content)
                MainNavigationRail(
                    currentTab = songUiState.songListType,
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

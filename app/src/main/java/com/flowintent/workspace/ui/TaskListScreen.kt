package com.flowintent.workspace.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Doorbell
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.Task
import com.flowintent.workspace.nav.ToDoNavTopBar
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.util.asString

@Preview(showBackground = true)
@Composable
fun ToDoListScreen() {
    ToDoNavTopBar { paddingValues ->
        Column {
            ListActionBar(paddingTopOffset = paddingValues)
            ListCardContent()
        }
    }
}

@Composable
fun ListActionBar(paddingTopOffset: PaddingValues) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingTopOffset),
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
                    .weight(1f),
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
            }
        }
    }
}

@Composable
private fun ListCardContent(viewModel: TaskViewModel = hiltViewModel()) {
    val taskList by viewModel.tasks.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(taskList) { task ->
            ToDoListCard(task, viewModel)
        }
    }
}

@Composable
private fun ToDoListCard(
    task: Task,
    viewModel: TaskViewModel
) {
    SwipeableCard(
        task = task,
        viewModel = viewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = task.content.asString(),
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

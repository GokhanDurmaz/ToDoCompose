package com.flowintent.workspace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.workspace.data.asString
import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.ui.vm.TaskViewModel

@Composable
fun ToDoListScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val taskList by viewModel.tasks.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
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
    val deleteResult by viewModel.deleteResult.collectAsStateWithLifecycle()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                viewModel.deleteTask(task)
                return@rememberSwipeToDismissBoxState deleteResult ?: false
            }
            false
        }
    )

    SwipeToDismissBox(
        modifier = Modifier.padding(bottom = 12.dp).clip(shape = RoundedCornerShape(12.dp)).fillMaxWidth(),
        state = swipeToDismissBoxState,
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {

                }
                SwipeToDismissBoxValue.EndToStart -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Remove",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove Item",
                            tint = Color.White
                        )
                    }
                }
                SwipeToDismissBoxValue.Settled -> { }
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,

    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = task.name,
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
}

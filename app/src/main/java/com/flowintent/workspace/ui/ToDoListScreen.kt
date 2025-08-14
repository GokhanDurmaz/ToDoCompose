package com.flowintent.workspace.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.local.LocalTaskDataProvider

@Preview(showBackground = true)
@Composable
fun ToDoListScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(LocalTaskDataProvider.allTasks) { task ->
            ToDoListCard(task = task)
        }
    }
}

@Composable
private fun ToDoListCard(task: Task) {
    Card(modifier = Modifier.fillMaxWidth()
        .height(100.dp)
        .padding(start = 12.dp, top = 12.dp, end = 12.dp)
    ) {
        Text(
            text = task.name,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
    }
}

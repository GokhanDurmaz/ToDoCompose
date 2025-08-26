package com.flowintent.workspace.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flowintent.workspace.data.local.LocalTaskDataProvider
import com.flowintent.workspace.data.local.Task
import com.flowintent.workspace.util.asString

@Preview(showBackground = true)
@Composable
fun ToDoHomeScreen() {
    ToDoAppContent()
}


@Composable
private fun ToDoAppContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Todo",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Box(modifier = Modifier) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(LocalTaskDataProvider.allTasks) { task ->
                        ToDoCard(task)
                    }
                }
            }
        }
    }
}

@Composable
private fun ToDoCard(task: Task) {
    Row {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(task.cardColor)
                )
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                            imageVector = task.icon,
                            contentDescription = task.title,
                            tint = Color(task.iconColor)
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            color = Color(task.textColor)
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = task.content.asString(),
                    color = Color(task.textColor)
                )
            }
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = task.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                )
                Text(
                    text = task.content.asString(),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                )
            }
        }
    }
}

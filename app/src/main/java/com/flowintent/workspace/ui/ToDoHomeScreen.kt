package com.flowintent.workspace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.SportsGymnastics
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
import com.flowintent.core.db.Task
import com.flowintent.workspace.data.local.LocalTaskDataProvider
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
            .fillMaxSize()
            .background(Color(0xffe9e9e9)),
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
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xffebeae8),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    item {
                        Text(
                            text = "Todo",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                        )
                    }
                    itemsIndexed(LocalTaskDataProvider.allTasks) { key, task ->
                        ToDoCard(task)
                        ToDoCardDescription(task)
                    }
                }
            }
        }
    }
}

@Composable
private fun ToDoCard(tasks: List<Task>) {
    Row {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(tasks[0].cardColor)
            )
        ) {
            Column {
                Row {
                    Icon(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        imageVector = Icons.Default.SportsGymnastics,
                        contentDescription = tasks[0].title,
                        tint = Color(tasks[0].iconColor)
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                        text = tasks[0].title,
                        fontWeight = FontWeight.Bold,
                        color = Color(tasks[0].textColor)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                text = tasks[0].content.asString(),
                color = Color(tasks[0].textColor)
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(tasks[1].cardColor)
            )
        ) {
            Column {
                Row {
                    Icon(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        imageVector = Icons.Default.ArtTrack,
                        contentDescription = tasks[1].title,
                        tint = Color(tasks[1].iconColor)
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                        text = tasks[1].title,
                        fontWeight = FontWeight.Bold,
                        color = Color(tasks[1].textColor)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                text = tasks[1].content.asString(),
                color = Color(tasks[1].textColor)
            )
        }
    }
}

private fun isBackgroundWhite(colorCode: Int): Boolean = "ffffffff".toLong(16).toInt() == colorCode

@Composable
private fun ToDoCardDescription(tasks: List<Task>) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = tasks[0].title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(start = 16.dp, top = 16.dp)
            )
            Text(
                text = tasks[0].content.asString(),
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = tasks[1].title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp)
            )
            Text(
                text = tasks[0].content.asString(),
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp)
            )
        }
    }
}

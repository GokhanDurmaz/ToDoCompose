package com.flowintent.workspace.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.TaskCategory
import com.flowintent.core.util.Resource
import com.flowintent.workspace.nav.ToDoNavTopBar
import com.flowintent.workspace.ui.vm.TaskCategoryViewModel
import com.flowintent.workspace.util.IconManager

@Preview(showBackground = true)
@Composable
fun ToDoHomeScreen() {
    ToDoNavTopBar { paddingValues ->
        ToDoAppContent(paddingValues = paddingValues)
    }
}

@Composable
private fun ToDoAppContent(
    taskCategoryViewModel: TaskCategoryViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val allCategoriesState by taskCategoryViewModel.getAllCategories()
        .collectAsStateWithLifecycle(initialValue = Resource.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Todo",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (val state = allCategoriesState) {
                    is Resource.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is Resource.Error -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                        }
                    }
                    is Resource.Success -> {
                        state.data.let { categories ->
                            items(categories) { task ->
                                ToDoCard(task)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ToDoCard(task: TaskCategory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(task.cardColor)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = IconManager.getIcon(task.icon.name),
                    contentDescription = null,
                    tint = Color(task.iconColor),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(task.textColor),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.content.text,
                color = Color(task.textColor),
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

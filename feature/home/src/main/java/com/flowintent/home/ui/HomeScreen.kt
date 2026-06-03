/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flowintent.home.ui.vm.HomeViewModel
import com.flowintent.uikit.theme.ToDoTheme

/**
 * Main Home screen listing categories.
 */
@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ToDoTheme {
        Scaffold { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(uiState.categories) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onCategoryClick(category.title) },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(category.cardColor)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(category.textColor)
                            )
                            Text(
                                text = category.content.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(category.textColor).copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

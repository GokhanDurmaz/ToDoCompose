/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.db.model.TaskCategory
import com.flowintent.home.R
import com.flowintent.home.ui.vm.HomeViewModel
import com.flowintent.uikit.theme.ToDoTheme
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_24
import com.flowintent.uikit.util.VAL_36
import com.flowintent.uikit.util.VAL_50
import com.flowintent.uikit.util.VAL_8
import com.flowintent.uikit.util.VAL_80
import java.util.Calendar

/**
 * Main Home screen listing categories with an improved dashboard design.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val username by homeViewModel.userName.collectAsStateWithLifecycle()

    ToDoTheme {
        Scaffold(
            topBar = {
                HomeTopBar()
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                WelcomeHeader(username)
                
                Text(
                    text = stringResource(R.string.categories_label),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = VAL_20.dp, vertical = VAL_12.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = VAL_16.dp, vertical = VAL_8.dp),
                    horizontalArrangement = Arrangement.spacedBy(VAL_16.dp),
                    verticalArrangement = Arrangement.spacedBy(VAL_16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.categories) { category ->
                        HomeCategoryCard(
                            category = category,
                            onClick = { onCategoryClick(category.title) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(VAL_80.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    TopAppBar(
        title = {},
        actions = {
            IconButton(onClick = { /* TODO: Search */ }) {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_desc))
            }
            IconButton(onClick = { /* TODO: Notifications */ }) {
                Icon(Icons.Default.Notifications, contentDescription = stringResource(R.string.notifications_desc))
            }
            Box(
                modifier = Modifier
                    .padding(end = VAL_12.dp)
                    .size(VAL_36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile_desc),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
private fun WelcomeHeader(username: String?) {
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> stringResource(R.string.good_morning)
        in 12..16 -> stringResource(R.string.good_afternoon)
        else -> stringResource(R.string.good_evening)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = VAL_20.dp, vertical = VAL_8.dp)
    ) {
        Text(
            text = "$greeting,",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = username ?: stringResource(R.string.default_user),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HomeCategoryCard(
    category: TaskCategory,
    onClick: () -> Unit
) {
    val icon = when (category.title) {
        "Gym" -> Icons.Default.FitnessCenter
        "Art" -> Icons.Default.Brush
        "Health" -> Icons.Default.Favorite
        else -> Icons.Default.ArtTrack
    }

    val (title, description) = when (category.title) {
        "Gym" -> stringResource(R.string.category_gym) to stringResource(R.string.category_gym_desc)
        "Art" -> stringResource(R.string.category_art) to stringResource(R.string.category_art_desc)
        "Health" -> stringResource(R.string.category_health) to stringResource(R.string.category_health_desc)
        "Sent" -> stringResource(R.string.category_sent) to stringResource(R.string.category_sent_desc)
        else -> category.title to category.content.text
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .clickable { onClick() },
        shape = RoundedCornerShape(VAL_24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(category.cardColor).copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(VAL_20.dp)
                    .align(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier
                        .size(VAL_50.dp)
                        .clip(RoundedCornerShape(VAL_12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(category.iconColor),
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(VAL_16.dp))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(category.textColor)
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(category.textColor).copy(alpha = 0.7f),
                    maxLines = 2
                )
            }
        }
    }
}

/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_36
import com.flowintent.uikit.util.VAL_4
import com.flowintent.uikit.util.VAL_50
import com.flowintent.uikit.util.VAL_60
import com.flowintent.uikit.util.VAL_8
import com.flowintent.uikit.util.VAL_80
import java.util.Calendar

/**
 * Main Home screen listing categories with an improved dashboard design.
 */
@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val username by homeViewModel.userName.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val columns = if (isLandscape) 4 else 2

    val onShareTasks = {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out my tasks: https://todoapp.flowintent.com/tasks/shared")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    ToDoTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onShareTasks,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                    modifier = Modifier.padding(bottom = VAL_60.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.category_sent)
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(
                        top = VAL_80.dp,
                        bottom = VAL_80.dp + 100.dp,
                        start = VAL_16.dp,
                        end = VAL_16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(VAL_16.dp),
                    verticalArrangement = Arrangement.spacedBy(VAL_16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(columns) }) {
                        WelcomeHeader(username)
                    }

                    item(span = { GridItemSpan(columns) }) {
                        Text(
                            text = stringResource(R.string.categories_label),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = VAL_4.dp, vertical = VAL_12.dp)
                        )
                    }

                    val filteredCategories = uiState.categories.filter { it.title != "Sent" }
                    items(
                        items = filteredCategories,
                        span = { category ->
                            val isWidget = category.title in listOf("Gym", "Art", "Health")
                            GridItemSpan(if (isWidget) columns else 1)
                        }
                    ) { category ->
                        val isWidget = category.title in listOf("Gym", "Art", "Health")
                        HomeCategoryCard(
                            category = category,
                            isExpanded = isWidget,
                            onClick = {
                                homeViewModel.onCategoryClicked(category.title)
                                onCategoryClick(category.title)
                            }
                        )
                    }
                }

                QuickActionContainer(
                    onSearchClick = { homeViewModel.onSearchClicked() },
                    onNotificationsClick = { homeViewModel.onNotificationsClicked() },
                    onProfileClick = { homeViewModel.onProfileClicked() },
                    modifier = Modifier
                        .align(AbsoluteAlignment.TopRight)
                        .padding(end = VAL_20.dp, top = VAL_12.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickActionContainer(
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(VAL_50.dp),
        color = if (isDark) {
            MaterialTheme.colorScheme.surfaceContainerHighest
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = if (isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)) else null,
        tonalElevation = if (isDark) 12.dp else 4.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = VAL_8.dp, vertical = VAL_4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(VAL_4.dp)
        ) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_desc)
                )
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.notifications_desc)
                )
            }
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile_desc)
                )
            }
        }
    }
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
    isExpanded: Boolean = false,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
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
        else -> category.title to category.content.text
    }

    val containerColor = if (isDark) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    } else {
        Color(category.cardColor)
    }

    val contentColor = if (isDark) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Color(category.textColor)
    }

    val accentColor = if (isDark) {
        Color(category.iconColor)
    } else {
        Color(category.iconColor)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isExpanded) 200.dp else 160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(VAL_24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = if (isDark) BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f)) else null
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(VAL_20.dp)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(VAL_50.dp)
                            .clip(RoundedCornerShape(VAL_12.dp))
                            .background(
                                if (isDark) accentColor.copy(alpha = 0.15f) 
                                else Color.White.copy(alpha = 0.25f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isDark) accentColor else Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(VAL_12.dp))
                    
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.85f),
                        maxLines = if (isExpanded) 3 else 2
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.width(VAL_20.dp))
                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .clip(RoundedCornerShape(VAL_12.dp))
                            .background(
                                if (isDark) MaterialTheme.colorScheme.surfaceContainerLowest 
                                else Color.White.copy(alpha = 0.2f)
                            )
                            .padding(VAL_12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (category.title) {
                            "Gym" -> {
                                Text("Weekly Goal", style = MaterialTheme.typography.labelSmall, color = contentColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { 0.6f },
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                    color = if (isDark) accentColor else Color.White,
                                    trackColor = contentColor.copy(alpha = 0.2f)
                                )
                                Text("3/5 days", style = MaterialTheme.typography.bodySmall, color = contentColor)
                            }
                            "Art" -> {
                                Text("Inspiration", style = MaterialTheme.typography.labelSmall, color = contentColor)
                                Text("Daily Sketch", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = contentColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape)
                                        .background(if (isDark) accentColor.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.2f)), 
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Brush, null, tint = if (isDark) accentColor else Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                            "Health" -> {
                                Text("Daily Metrics", style = MaterialTheme.typography.labelSmall, color = contentColor)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Favorite, 
                                        null, 
                                        tint = if (isDark) Color.Red.copy(alpha = 0.6f) else Color.White, 
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("72 bpm", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = contentColor)
                                }
                                Text("Optimal", style = MaterialTheme.typography.bodySmall, color = contentColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

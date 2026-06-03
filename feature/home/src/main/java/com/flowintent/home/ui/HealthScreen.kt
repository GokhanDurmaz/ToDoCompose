/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Health Tracker", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = VAL_16.dp),
            verticalArrangement = Arrangement.spacedBy(VAL_16.dp)
        ) {
            item { HealthSummaryCard() }
            item { Text("Daily Metrics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(healthMetrics) { metric ->
                HealthMetricItem(metric)
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun HealthSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = VAL_8.dp),
        shape = RoundedCornerShape(VAL_20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(VAL_20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(VAL_8.dp))
                Text("Health Score", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(VAL_12.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("85", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Text("/100", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 8.dp))
            }
            Text("You are doing great! Keep it up.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun HealthMetricItem(metric: HealthMetric) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(VAL_16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(VAL_16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(metric.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(metric.icon, contentDescription = null, tint = metric.color)
            }
            Spacer(modifier = Modifier.width(VAL_16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(metric.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(metric.value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(metric.status, fontWeight = FontWeight.Medium, color = metric.color, fontSize = 12.sp)
        }
    }
}

private data class HealthMetric(val title: String, val value: String, val icon: ImageVector, val color: Color, val status: String)

private val healthMetrics = listOf(
    HealthMetric("Heart Rate", "72 BPM", Icons.Default.MonitorHeart, Color(0xFFF44336), "Normal"),
    HealthMetric("Water Intake", "1.5L / 2.5L", Icons.Default.WaterDrop, Color(0xFF2196F3), "In Progress"),
    HealthMetric("Calories Burned", "450 kcal", Icons.Default.LocalFireDepartment, Color(0xFFFF9800), "Good"),
    HealthMetric("Sleep", "7h 20m", Icons.Default.Favorite, Color(0xFF9C27B0), "Optimal")
)

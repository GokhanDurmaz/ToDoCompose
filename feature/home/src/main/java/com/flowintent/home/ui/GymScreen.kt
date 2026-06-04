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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flowintent.home.R
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymScreen(onBack: () -> Unit = {}) {
    val workoutList = listOf(
        Workout(stringResource(R.string.morning_yoga), stringResource(R.string.duration_mins_format, "30"), stringResource(R.string.intensity_low), Color(0xFF4CAF50)),
        Workout(stringResource(R.string.weight_lifting), stringResource(R.string.duration_mins_format, "45"), stringResource(R.string.intensity_high), Color(0xFFF44336)),
        Workout(stringResource(R.string.cardio_session), stringResource(R.string.duration_mins_format, "20"), stringResource(R.string.intensity_medium), Color(0xFF2196F3)),
        Workout(stringResource(R.string.abs_workout), stringResource(R.string.duration_mins_format, "15"), stringResource(R.string.intensity_medium), Color(0xFFFF9800))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.gym_fitness_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = stringResource(R.string.back_desc), modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(VAL_16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_workout_desc))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = VAL_16.dp),
            verticalArrangement = Arrangement.spacedBy(VAL_16.dp)
        ) {
            item { WorkoutProgressCard() }
            item { Text(stringResource(R.string.today_plan_label), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(workoutList) { workout ->
                WorkoutItem(workout)
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun WorkoutProgressCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VAL_8.dp),
        shape = RoundedCornerShape(VAL_20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(VAL_20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(VAL_8.dp))
                Text(stringResource(R.string.weekly_goal_label), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(VAL_12.dp))
            LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(VAL_8.dp))
            Text(stringResource(R.string.workouts_completed_format, 3, 5), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun WorkoutItem(workout: Workout) {
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
                    .clip(RoundedCornerShape(VAL_12.dp))
                    .background(workout.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = workout.color)
            }
            Spacer(modifier = Modifier.width(VAL_16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(workout.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(workout.duration, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(workout.intensity, fontWeight = FontWeight.Medium, color = workout.color, fontSize = 12.sp)
        }
    }
}

private data class Workout(val name: String, val duration: String, val intensity: String, val color: Color)

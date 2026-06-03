/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.home

import com.flowintent.core.db.model.TaskCategory
import com.flowintent.core.db.model.TaskContent
import com.flowintent.core.db.model.TaskIcon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to retrieve home categories like Gym, Art, and Health.
 */
class GetHomeCategoriesUseCase @Inject constructor() {
    operator fun invoke(): Flow<List<TaskCategory>> = flow {
        val categories = listOf(
            TaskCategory(
                title = "Gym",
                content = TaskContent("Physical activities and workouts"),
                icon = TaskIcon("vector", "FitnessCenter"),
                cardColor = 0xFFE3F2FD,
                iconColor = 0xFF1976D2,
                textColor = 0xFF0D47A1
            ),
            TaskCategory(
                title = "Art",
                content = TaskContent("Creative expression and design"),
                icon = TaskIcon("vector", "Palette"),
                cardColor = 0xFFF3E5F5,
                iconColor = 0xFF7B1FA2,
                textColor = 0xFF4A148C
            ),
            TaskCategory(
                title = "Health",
                content = TaskContent("Medical and wellness tracking"),
                icon = TaskIcon("vector", "HealthAndSafety"),
                cardColor = 0xFFE8F5E9,
                iconColor = 0xFF388E3C,
                textColor = 0xFF1B5E20
            )
        )
        emit(categories)
    }
}

package com.flowintent.workspace.data.remote

import com.flowintent.workspace.data.remote.services.AnalyticsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AnalyticsServiceImpl: AnalyticsService {
    override fun getAnalyticStatistics(users: List<String>): Flow<List<String>> {
        return flowOf(users)
    }
}
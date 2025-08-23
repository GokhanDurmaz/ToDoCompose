package com.flowintent.workspace.data.remote

import com.flowintent.workspace.data.remote.services.AnalyticsService
import kotlinx.coroutines.flow.Flow

class AnalyticsServiceImpl: AnalyticsService {
    override fun getAnalyticStatistics(analyticsType: AnalyticsType): Flow<List<String>> {
        TODO("Not yet implemented")
    }
}
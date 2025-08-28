package com.flowintent.workspace.data.remote

import com.flowintent.core.network.AnalyticsType
import com.flowintent.core.network.services.AnalyticsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AnalyticsServiceImpl: AnalyticsService {

    override fun getAnalyticStatistics(analyticsType: AnalyticsType): Flow<List<String>> {
        return flowOf(listOf())
    }
}

package com.flowintent.workspace.data.remote.services

import com.flowintent.workspace.data.remote.AnalyticsType
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalyticsService {
    @GET("users/statistics")
    fun getAnalyticStatistics(@Path("user") analyticsType: AnalyticsType): Flow<List<String>>
}
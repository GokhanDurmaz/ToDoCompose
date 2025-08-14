package com.flowintent.workspace.data.remote.services

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalyticsService {
    @GET("users/statistics")
    fun getAnalyticStatistics(@Path("user") users: List<String>): Flow<List<String>>
}
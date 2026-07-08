/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.network.network.interceptors

import com.flowintent.core.util.AnalyticsEvent
import com.flowintent.core.util.AppEventTracker
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkErrorInterceptor @Inject constructor(
    private val eventTracker: AppEventTracker
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            eventTracker.logException(e)
            eventTracker.logMessage("Network call failed: ${request.url}")
            throw e
        }

        if (!response.isSuccessful) {
            eventTracker.logEvent(
                AnalyticsEvent.NetworkError(
                    url = request.url.toString(),
                    code = response.code,
                    method = request.method,
                    errorMessage = response.message
                )
            )
            eventTracker.logMessage("HTTP Error ${response.code} for ${request.url}")
        }

        return response
    }
}

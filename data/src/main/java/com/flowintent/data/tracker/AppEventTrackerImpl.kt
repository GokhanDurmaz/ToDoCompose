/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.data.tracker

import android.os.Bundle
import com.flowintent.core.util.AnalyticsEvent
import com.flowintent.core.util.AppEventTracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppEventTrackerImpl @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) : AppEventTracker {

    override fun logEvent(event: AnalyticsEvent) {
        val nonNullParams = event.params.mapNotNull { (key, value) ->
            value?.let { key to it }
        }.toMap()
        logEvent(event.name, nonNullParams)
    }

    override fun logEvent(name: String, params: Map<String, Any>?) {
        val bundle = params?.let {
            Bundle().apply {
                it.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Double -> putDouble(key, value)
                        is Boolean -> putBoolean(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }
        }
        analytics.logEvent(name, bundle)
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun logMessage(message: String) {
        crashlytics.log(message)
    }
}

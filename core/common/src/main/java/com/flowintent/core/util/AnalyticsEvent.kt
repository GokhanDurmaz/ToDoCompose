/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.util

sealed class AnalyticsEvent(val name: String) {
    abstract val params: Map<String, Any?>

    // Auth Events
    data class LoginSuccess(val method: String) : AnalyticsEvent("login_success") {
        override val params = mapOf("method" to method)
    }

    data class LoginError(val method: String, val errorCode: String?) : AnalyticsEvent("login_error") {
        override val params = mapOf("method" to method, "error_code" to errorCode)
    }

    data class SignUpSuccess(val method: String) : AnalyticsEvent("sign_up_success") {
        override val params = mapOf("method" to method)
    }

    data class SignUpError(val method: String, val errorCode: String?) : AnalyticsEvent("sign_up_error") {
        override val params = mapOf("method" to method, "error_code" to errorCode)
    }

    // Navigation Events
    data class ScreenView(val screenName: String, val screenClass: String?) : AnalyticsEvent("screen_view_custom") {
        override val params = mapOf("screen_name" to screenName, "screen_class" to screenClass)
    }

    // Network Events
    data class NetworkError(
        val url: String,
        val code: Int,
        val method: String,
        val errorMessage: String?
    ) : AnalyticsEvent("network_error") {
        override val params = mapOf(
            "url_path" to url.substringBefore("?"), // PII safe
            "http_code" to code,
            "method" to method,
            "error_message" to errorMessage
        )
    }

    // Feature Funnels
    data class TaskCreated(val source: String, val isAiGenerated: Boolean) : AnalyticsEvent("task_created") {
        override val params = mapOf("source" to source, "is_ai_generated" to isAiGenerated)
    }
}

/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.util

interface AppEventTracker {
    fun logEvent(name: String, params: Map<String, Any>? = null)
    fun logException(throwable: Throwable)
    fun logMessage(message: String)
}

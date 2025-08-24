package com.flowintent.core.network

enum class AnalyticsType(val eventName: String) {
    TASK_ADDED("task_added"),
    TASK_COMPLETED("task_completed"),
    TASK_DELETED(""),
    TASK_UPDATED("task_updated"),
    APP_OPENED("app_opened"),
    CATEGORY_ADDED("category_added"),
    NOTIFICATION_CLICKED("notification_clicked"),
    FILTER_APPLIED("filter_applied"),
    ERROR_OCCURRED("error_occurred");

    companion object {
        fun fromEventName(eventName: String): AnalyticsType? {
            return entries.find { it.eventName == eventName }
        }
    }
}

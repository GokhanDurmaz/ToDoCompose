/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.data.secure

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import com.flowintent.core.util.RootDetectionUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityInterceptor @Inject constructor() {
    fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (RootDetectionUtil.isDeviceRooted()) {
                    Toast.makeText(
                        activity,
                        "Security Alert: Rooted device. App shutting down.",
                        Toast.LENGTH_LONG
                    ).show()
                    activity.finish()
                }
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}

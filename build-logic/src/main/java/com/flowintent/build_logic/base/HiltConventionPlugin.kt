/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.build_logic.base

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            val libs = extensions.findByType<VersionCatalogsExtension>()?.named("libs")
                ?: rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                "implementation"(libs.findLibrary("hilt.android").get())
                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}

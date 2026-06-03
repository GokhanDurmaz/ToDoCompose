/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.build_logic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            val configureDependencies = {
                dependencies {
                    "implementation"(libs.findLibrary("androidx-runtime").get())
                    "implementation"(libs.findLibrary("ui").get())
                    "implementation"(libs.findLibrary("androidx-material3").get())
                    "implementation"(libs.findLibrary("androidx-material3-android").get())
                    "implementation"(libs.findLibrary("androidx-appcompat").get())
                    "implementation"(libs.findLibrary("androidx-foundation").get())
                    "implementation"(libs.findLibrary("androidx-foundation-layout").get())
                    "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
                    "implementation"(libs.findLibrary("androidx-material-icons-core").get())
                    "implementation"(libs.findLibrary("androidx-material-icons-extended").get())
                    "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
                }
            }

            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    buildFeatures {
                        compose = true
                    }
                }
                configureDependencies()
            }
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildFeatures {
                        compose = true
                    }
                }
                configureDependencies()
            }
        }
    }
}

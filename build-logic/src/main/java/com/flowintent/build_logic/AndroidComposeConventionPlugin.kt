package com.flowintent.build_logic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            val configureCompose = {
                extensions.findByType<CommonExtension<*, *, *, *, *, *>>()?.apply {
                    buildFeatures {
                        compose = true
                    }
                }

                dependencies {
                    "implementation"(libs.findLibrary("androidx-runtime").get())
                    "implementation"(libs.findLibrary("ui").get())
                    "implementation"(libs.findLibrary("androidx-material3").get())
                    "implementation"(libs.findLibrary("androidx-material3-android").get())
                    "implementation"(libs.findLibrary("androidx-appcompat").get())
                    "implementation"(libs.findLibrary("androidx-foundation-layout").get())
                    "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
                    "implementation"(libs.findLibrary("androidx-material-icons-core").get())
                    "implementation"(libs.findLibrary("androidx-material-icons-extended").get())
                    "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
                }
            }

            pluginManager.withPlugin("com.android.application") { configureCompose() }
            pluginManager.withPlugin("com.android.library") { configureCompose() }
        }
    }
}

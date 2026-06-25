package com.flowintent.build_logic.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class CoreSharedConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.application") {
                applySharedConfig(target)
            }
            pluginManager.withPlugin("com.android.library") {
                applySharedConfig(target)
            }

            if (path != ":app" && path != ":schema" && file("src").exists()) {
                pluginManager.apply("com.android.library")
            }
        }
    }

    private fun applySharedConfig(target: Project) {
        with(target) {
            pluginManager.apply("flowintent.android.base")
            pluginManager.apply("flowintent.hilt")

            val catalog = extensions.findByType<VersionCatalogsExtension>()?.named("libs")
                ?: rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                val coreKtxProvider = catalog.findLibrary("androidx.core.ktx")
                if (coreKtxProvider.isPresent) {
                    "implementation"(coreKtxProvider.get())
                }
            }
        }
    }
}
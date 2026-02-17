package com.flowintent.build_logic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.configure
import com.google.devtools.ksp.gradle.KspExtension

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                "implementation"(libs.findLibrary("androidx-room-runtime").get())
                "implementation"(libs.findLibrary("androidx-room-ktx").get())
                "testImplementation"(libs.findLibrary("androidx-room-testing").get())
                "ksp"(libs.findLibrary("androidx-room-compiler").get())
            }

            extensions.configure<KspExtension> {
                arg("room.schemaLocation", "$projectDir/schema")
            }
        }
    }
}
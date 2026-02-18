package com.flowintent.build_logic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidBaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.android")

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                    freeCompilerArgs.add("-Xannotation-default-target=param-property")
                }
            }

            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    configureSharedAndroidOptions(this)
                }
            }
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    configureSharedAndroidOptions(this)
                }
            }
        }
    }

    private fun Project.configureSharedAndroidOptions(commonExtension: CommonExtension<*, *, *, *, *, *>) {
        commonExtension.apply {
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
                isCoreLibraryDesugaringEnabled = true
            }

            dependencies.add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.0.4")
        }
    }
}

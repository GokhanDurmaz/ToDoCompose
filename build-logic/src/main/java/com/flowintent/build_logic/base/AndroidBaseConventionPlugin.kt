package com.flowintent.build_logic.base

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidBaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.findByType<VersionCatalogsExtension>()?.named("libs")
                ?: rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply("kotlinx-serialization")

            val addFirebaseDependencies = {
                dependencies {
                    "implementation"(platform(libs.findLibrary("firebase-bom").get()))
                    "implementation"(libs.findLibrary("firebase-crashlytics").get())
                    "implementation"(libs.findLibrary("firebase-analytics").get())
                }
            }

            pluginManager.withPlugin("com.android.application") {
                pluginManager.apply("com.google.gms.google-services")
                pluginManager.apply("com.google.firebase.crashlytics")
                addFirebaseDependencies()
            }

            pluginManager.withPlugin("com.android.library") {
                addFirebaseDependencies()
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                    freeCompilerArgs.add("-Xannotation-default-target=param-property")
                }
            }

            tasks.withType<Test>().configureEach {
                jvmArgs("-Xshare:off")
            }

            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    compileSdk = 36
                    defaultConfig {
                        minSdk = 24
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_17
                        targetCompatibility = JavaVersion.VERSION_17
                        isCoreLibraryDesugaringEnabled = true
                    }
                    buildTypes {
                        getByName("release") {
                            isMinifyEnabled = false
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                "proguard-rules.pro"
                            )
                        }
                    }
                    dependencies.add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.1.5")
                }
            }
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    compileSdk = 36
                    defaultConfig {
                        minSdk = 24
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_17
                        targetCompatibility = JavaVersion.VERSION_17
                        isCoreLibraryDesugaringEnabled = true
                    }
                    buildTypes {
                        getByName("release") {
                            isMinifyEnabled = false
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                "proguard-rules.pro"
                            )
                        }
                    }
                    defaultConfig {
                        if (file("consumer-rules.pro").exists()) {
                            consumerProguardFiles("consumer-rules.pro")
                        }
                    }
                    dependencies.add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.1.5")
                }
            }
        }
    }
}
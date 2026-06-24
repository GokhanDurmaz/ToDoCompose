// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(libs.plugins.android.application.get().pluginId) version libs.versions.agp.get() apply false
    id(libs.plugins.android.library.get().pluginId) version libs.versions.agp.get() apply false
    id(libs.plugins.kotlin.android.get().pluginId) version libs.versions.kotlin.get() apply false
    id(libs.plugins.compose.compiler.get().pluginId) version libs.versions.kotlin.get() apply false

    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) version libs.versions.hilt apply false
    alias(libs.plugins.google.services) version libs.versions.googleService apply false

    alias(libs.plugins.flowintent.detekt) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

apply(from = "jacoco.gradle.kts")

tasks.register("clean") {
    delete(rootProject.layout.buildDirectory)

    delete("build-logic/build")
    delete("build-logic/src/main/generated")
}

defaultTasks("buildApp")

tasks.register("runAllTests") {
    group = "verification"
    description = "Runs all unit tests for all modules."
    subprojects.forEach { subproject ->
        subproject.tasks.withType<Test>().forEach { task ->
            dependsOn(task)
        }
    }
}

tasks.register("buildAppDebug") {
    group = "build"
    description = "Builds the app and its dependencies with verification"
    dependsOn(tasks.named("runAllTests"))
    dependsOn(":app:detekt")
    dependsOn(":app:assembleDebug")
}

tasks.register("buildAppRelease") {
    group = "build"
    description = "Builds the app and its dependencies for release"
    dependsOn(tasks.named("runAllTests"))
    dependsOn(":app:detekt")
    dependsOn(":app:assembleRelease")
}

subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            val isDebugBuild = gradle.startParameter.taskNames.any { it.contains("buildAppDebug", ignoreCase = true) }
            ignoreFailures = isDebugBuild
        }
    }
}

subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.google.protobuf") {
                useVersion("3.25.1")
            }
        }
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
}
tasks.register("clean") {
    delete {
        getLayout().buildDirectory
    }
}

defaultTasks("buildApp")

tasks.register("runAllTests") {
    group = "verification"
    description = "Runs all unit tests for the specified modules."
    dependsOn(":test:testDebugUnitTest")
}

tasks.register("buildAppDebug") {
    group = "build"
    description = "Builds both app and core modules"
    dependsOn(tasks.named("runAllTests"))

    dependsOn(":app:assembleDebug")
    dependsOn(":core:assembleDebug")
    dependsOn(":data:assembleDebug")
}

tasks.register("buildAppRelease") {
    group = "build"
    description = "Builds both app and core modules"
    dependsOn(tasks.named("runAllTests"))

    dependsOn(":app:assembleRelease")
    dependsOn(":core:assembleRelease")
    dependsOn(":data:assembleRelease")
}


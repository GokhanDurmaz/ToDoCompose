pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "ToDo App"
include(":app")
include(":core:common")
include(":core:navigation")
include(":core:network")
include(":feature:auth")
include(":feature:profile")
include(":feature:settings")
include(":feature:home")
include(":feature:uikit")
include(":test")
include(":data")
include(":schema")

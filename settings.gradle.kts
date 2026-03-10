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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ToDo App"
include(":app")
include(":core:common")
include(":core:navigation")
include(":core:network")
include(":feature:auth")
include(":feature:profile")
include(":feature:settings")
include(":feature:uikit")
include(":test")
include(":data")
include(":schema")

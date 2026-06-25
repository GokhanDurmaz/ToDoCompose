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

include(":app", ":test", ":data", ":schema", ":core", ":feature")

val multiModuleContainers = listOf("core", "feature")

multiModuleContainers.forEach { containerName ->
    val containerFile = rootDir.resolve("$containerName.gradle.kts")
    if (containerFile.exists()) {
        include(":$containerName")
        project(":$containerName").buildFileName = containerFile.name
    }

    rootDir.resolve(containerName).listFiles()
        ?.filter { it.isDirectory && it.name != "build" }
        ?.forEach { subDir ->
            val modulePath = ":$containerName:${subDir.name}"
            include(modulePath)

            val customBuildFile = subDir.resolve("${subDir.name}.gradle.kts")
            if (customBuildFile.exists()) {
                project(modulePath).buildFileName = customBuildFile.name
            }
        }
}
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

// Ensure local.properties has required keys for developers
if (System.getenv("CI") == null) {
    val localPropertiesFile = File(rootDir, "local.properties")
    val requiredKeys = listOf(
        "API_KEY", "BASE_URL", "GROQ_API_KEY", "XOR_KEY",
        "SUPABASE_URL", "SUPABASE_API_KEY",
        "KEY_ALIAS", "KEY_PASSWORD", "STORE_PASSWORD"
    )
    val existingContent = if (localPropertiesFile.exists()) localPropertiesFile.readText() else ""
    val missingKeys = requiredKeys.filter { !existingContent.contains("$it=") }
    if (missingKeys.isNotEmpty()) {
        val newLines = missingKeys.joinToString(
            "\n",
            prefix = if (existingContent.isNotEmpty() && !existingContent.endsWith("\n")) "\n\n" else "\n"
        ) { "$it=..." }
        localPropertiesFile.appendText(newLines)
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
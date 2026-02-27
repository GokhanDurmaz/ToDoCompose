package com.flowintent.build_logic.secret

import groovy.json.JsonSlurper
import org.gradle.api.Project

fun Project.loadSecretConfig(): Map<String, Any> {
    val configFile = rootProject.file("secrets-config.json")

    if (!configFile.exists()) {
        return emptyMap()
    }

    val parsed = runCatching { 
        JsonSlurper().parseText(configFile.readText()) 
    }.getOrNull() as? Map<*, *> ?: return emptyMap()

    return parsed.entries.associate { 
        it.key.toString() to (it.value ?: "") 
    }
}

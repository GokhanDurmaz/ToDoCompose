package com.flowintent.build_logic.secret

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.util.Properties

class AndroidSecretPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create<AndroidSecretExtension>("androidSecrets").apply {
                cmakePath.convention("src/main/cpp/CMakeLists.txt")
                cmakeVersion.convention("3.22.1")
            }

            val projectLayout = layout

            val generateSecrets = tasks.register<GenerateSecretsTask>("generateSecrets") {
                val localFile = rootProject.file("local.properties")

                secrets.set(provider {
                    val properties = Properties()
                    if (localFile.exists()) {
                        localFile.inputStream().use { properties.load(it) }
                    }

                    val secretsMap = mutableMapOf<String, String>()
                    val keysFromConfig = extension.secretKeys.getOrElse(emptyList())

                    keysFromConfig.forEach { key ->
                        val value = System.getenv(key) ?: properties.getProperty(key) ?: ""
                        if (value.isNotEmpty()) {
                            secretsMap[key] = value
                        }
                    }
                    secretsMap
                })

                outputFile.set(layout.projectDirectory.file("src/main/cpp/secrets.h"))
            }

            val generateCMake = tasks.register("generateCMakeLists") {
                val cmakePathProvider = extension.cmakePath
                val libNameProvider = extension.libName
                val cppFileNameProvider = extension.cppFileName
                val cmakeVersionProvider = extension.cmakeVersion
                val linkedLibsProvider = extension.linkedLibraries

                val outputDir = projectLayout.projectDirectory

                doLast {
                    val path = cmakePathProvider.get()
                    val cmakeFile = outputDir.file(path).asFile

                    val content = StringBuilder().apply {
                        appendLine("cmake_minimum_required(VERSION ${cmakeVersionProvider.get()})")
                        appendLine("project(\"${libNameProvider.get()}\")")
                        appendLine("")
                        appendLine("add_library(${libNameProvider.get()} SHARED ${cppFileNameProvider.get()})")
                        appendLine("")

                        linkedLibsProvider.get().forEach { lib ->
                            appendLine("find_library($lib-lib $lib)")
                        }

                        val linkedLibsString = linkedLibsProvider.get().joinToString(" ") { "\${$it-lib}" }
                        appendLine("target_link_libraries(${libNameProvider.get()} $linkedLibsString)")
                    }.toString()

                    cmakeFile.parentFile.mkdirs()
                    cmakeFile.writeText(content)
                }
            }

            val cmakePath = extension.cmakePath.get()
            val cmakeVersion= extension.cmakeVersion.get()
            extensions.configure<LibraryExtension> {
                externalNativeBuild {
                    cmake {
                        path = file(cmakePath)
                        version = cmakeVersion
                    }
                }
            }

            tasks.configureEach {
                if (name.contains("externalNativeBuild", ignoreCase = true)) {
                    dependsOn(generateSecrets, generateCMake)
                }
            }
            tasks.named("preBuild") {
                dependsOn(generateSecrets, generateCMake)
            }
        }
    }
}

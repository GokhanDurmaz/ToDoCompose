package com.flowintent.build_logic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            val taskName = "jacocoTestReport"

            tasks.register(taskName, JacocoReport::class.java) {
                group = "Reporting"
                description = "Merge all sub-modules coverage reports in :test module."

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }

                val fileFilter = listOf(
                    "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
                    "**/*Test*.*", "android/**/*.*", "**/*_MembersInjector.class",
                    "**/Dagger*.*", "**/*_Factory.class", "**/*_Provide*Factory.class",
                    "**/*_ViewBinding*.*", "**/AutoValue_*.*", "**/R2.class", "**/R2$*.class",
                    "**/*Directions$*", "**/*Directions.*", "**/*Binding.*",
                    "**/*\$Lambda\$*.*", "**/*Companion*.*", "**/*Module*.*",
                    "**/*Inject*.*", "**/*_*.class"
                )

                val classDirectoriesCollection: ConfigurableFileCollection = objects.fileCollection()
                val sourceDirectoriesCollection: ConfigurableFileCollection = objects.fileCollection()
                val executionDataCollection: ConfigurableFileCollection = objects.fileCollection()

                rootProject.subprojects.forEach { subproject ->
                    if (subproject.name == project.name) return@forEach

                    subproject.afterEvaluate {
                        val isAndroid = pluginManager.hasPlugin("com.android.application") ||
                                pluginManager.hasPlugin("com.android.library")

                        if (isAndroid) {
                            val javacTree = layout.buildDirectory.dir("intermediates/javac/debug")
                            val kotlinTree = layout.buildDirectory.dir("tmp/kotlin-classes/debug")
                            classDirectoriesCollection.from(
                                fileTree(javacTree) { exclude(fileFilter) },
                                fileTree(kotlinTree) { exclude(fileFilter) }
                            )
                        } else {
                            val javaMainTree = layout.buildDirectory.dir("classes/java/main")
                            val kotlinMainTree = layout.buildDirectory.dir("classes/kotlin/main")
                            classDirectoriesCollection.from(
                                fileTree(javaMainTree) { exclude(fileFilter) },
                                fileTree(kotlinMainTree) { exclude(fileFilter) }
                            )
                        }

                        sourceDirectoriesCollection.from(
                            subproject.files("${subproject.projectDir}/src/main/java"),
                            subproject.files("${subproject.projectDir}/src/main/kotlin")
                        )

                        if (isAndroid) {
                            val execFile = layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
                            if (execFile.get().asFile.exists()) {
                                executionDataCollection.from(execFile)
                            }
                        } else {
                            val execFile = layout.buildDirectory.file("jacoco/test.exec")
                            if (execFile.get().asFile.exists()) {
                                executionDataCollection.from(execFile)
                            }
                        }
                    }

                    subproject.tasks.matching { it.name == "testDebugUnitTest" || it.name == "test" }.configureEach {
                        this@register.dependsOn(this)
                    }
                }

                classDirectories.setFrom(classDirectoriesCollection)
                sourceDirectories.setFrom(sourceDirectoriesCollection)
                executionData.setFrom(executionDataCollection)
            }
        }
    }
}

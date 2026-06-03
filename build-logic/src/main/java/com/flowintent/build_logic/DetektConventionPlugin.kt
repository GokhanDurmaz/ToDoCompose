/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.build_logic

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import java.io.File

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            // Modern configuration for reporting extension as per Gradle 9.x research
            extensions.configure<ReportingExtension> {
                baseDirectory.set(layout.buildDirectory.dir("reports"))
            }

            val detektExtension = extensions.getByType<DetektExtension>()

            detektExtension.apply {
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                ignoreFailures = false
                basePath = rootProject.projectDir.absolutePath
            }

            tasks.withType<Detekt>().configureEach {
                // Use modern Provider-based API for reports directory
                reportsDir.set(layout.buildDirectory.dir("reports/detekt").map { it.asFile })

                reports {
                    html.required.set(true)
                    html.outputLocation.set(layout.buildDirectory.file("reports/detekt/${project.name}.html"))
                    xml.required.set(true)
                    xml.outputLocation.set(layout.buildDirectory.file("reports/detekt/${project.name}.xml"))
                    sarif.required.set(true)
                    sarif.outputLocation.set(layout.buildDirectory.file("reports/detekt/${project.name}.sarif"))
                    txt.required.set(false)
                    md.required.set(false)
                }

                jdkHome.set(File(System.getProperty("java.home")))
            }

            tasks.named("check") {
                dependsOn(tasks.withType<Detekt>())
            }
        }
    }
}

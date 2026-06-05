subprojects {
    apply(plugin = "jacoco")

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    val taskName = "jacocoTestReport"

    // Use afterEvaluate to ensure all plugins (like java-library) have had a chance to create their tasks
    afterEvaluate {
        val testTaskName = if (plugins.hasPlugin("com.android.library") || plugins.hasPlugin("com.android.application")) {
            "testDebugUnitTest"
        } else {
            "test"
        }

        if (tasks.findByName(testTaskName) == null) return@afterEvaluate

        val reportTask = if (tasks.findByName(taskName) == null) {
            tasks.register<JacocoReport>(taskName)
        } else {
            tasks.named<JacocoReport>(taskName)
        }

        reportTask.configure {
            dependsOn(testTaskName)
            group = "Reporting"
            description = "Generate Jacoco coverage reports"

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            val fileFilter = listOf(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "android/**/*.*",
                "**/*_MembersInjector.class",
                "**/Dagger*.*",
                "**/*_Factory.class",
                "**/*_Provide*Factory.class",
                "**/*_ViewBinding*.*",
                "**/AutoValue_*.*",
                "**/R2.class",
                "**/R2$*.class",
                "**/*Directions$*",
                "**/*Directions.*",
                "**/*Binding.*"
            )

            val debugTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
                exclude(fileFilter)
            } + fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
                exclude(fileFilter)
            } + fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
                exclude(fileFilter)
            }

            val mainSrc = "${project.projectDir}/src/main/java"

            sourceDirectories.setFrom(files(mainSrc))
            classDirectories.setFrom(files(debugTree))
            
            // Collect execution data from both Android and Java standard paths
            executionData.setFrom(fileTree(layout.buildDirectory) {
                include(
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    "jacoco/test.exec"
                )
            })
        }
    }
}

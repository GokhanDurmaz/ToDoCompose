import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.flowintent.android.compose)
    alias(libs.plugins.flowintent.detekt)
}

val gitHashProvider = providers.exec {
    commandLine("git", "rev-parse", "--short", "HEAD")
    isIgnoreExitValue = true
}.standardOutput.asText.map { it.trim() }.orElse("unknown")

val versionNameProvider = providers.environmentVariable("VERSION_NAME")
    .orElse(providers.gradleProperty("VERSION_NAME"))
    .orElse("V1.0.0")

val versionCodeProvider = providers.environmentVariable("VERSION_CODE")
    .map { it.toInt() }
    .orElse(100)

android {
    namespace = "com.flowintent.workspace"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.flowintent.workspace"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        androidResources {
            localeFilters += listOf("en", "ar", "de", "es", "fr", "ja", "pt", "ru", "zh", "tr")
        }
    }

    signingConfigs {
        val localProperties = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) {
                file.inputStream().use { load(it) }
            }
        }

        val keyAliasVal = providers.environmentVariable("KEY_ALIAS")
            .orElse(providers.gradleProperty("KEY_ALIAS"))
            .getOrElse(localProperties.getProperty("KEY_ALIAS") ?: "default_alias")

        val keyPasswordVal = providers.environmentVariable("KEY_PASSWORD")
            .orElse(providers.gradleProperty("KEY_PASSWORD"))
            .getOrElse(localProperties.getProperty("KEY_PASSWORD") ?: "")

        val storePasswordVal = providers.environmentVariable("STORE_PASSWORD")
            .orElse(providers.gradleProperty("STORE_PASSWORD"))
            .getOrElse(localProperties.getProperty("STORE_PASSWORD") ?: "")

        create("release") {
            keyAlias = keyAliasVal
            keyPassword = keyPasswordVal
            storeFile = file("release-keystore.jks")
            storePassword = storePasswordVal
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }

}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            val gitHash = gitHashProvider.get()
            val verName = versionNameProvider.get().replace(".", "_")
            val verCode = versionCodeProvider.get()
            val variantName = variant.name

            output.outputFileName.set("todo_app_${verName}_${verCode}_${gitHash}_${variantName}.apk")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3.window.size.class1.android)
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:uikit"))
    implementation(project(":data"))

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)
    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.ui)

    // Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    debugImplementation(libs.androidx.ui.test.manifest)
    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary.android)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.paging.compose)

    // Kotlin reflection library
    implementation(libs.kotlin.reflect)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

project(":").tasks.named("buildAppDebug") {
    finalizedBy(":app:deployApk")
}

project(":").tasks.named("buildAppRelease") {
    finalizedBy(":app:deployApk")
}

fun getRequestedVariant(): String {
    val tasks = gradle.startParameter.taskNames
    return when {
        tasks.any { it.contains("Release", ignoreCase = true) } -> "release"
        else -> "debug"
    }
}

val rootDirFile = rootProject.rootDir
val deployScriptPath: String = rootProject.file("deploy.sh").absolutePath
val vName = versionNameProvider
val vCode = versionCodeProvider

tasks.register<Exec>("deployApk") {
    description = "Deploys the built APK to a connected device using deploy.sh."
    group = "deployment"

    val requestedVariant = project.findProperty("variant")?.toString() ?: "debug"
    val scriptPath = deployScriptPath
    val verNameValue = vName.get()
    val verCodeValue = vCode.get()
    val workingDirectory = rootDirFile

    workingDir = workingDirectory

    // Use "sh" by default, allowing override via -PdeployShell=...
    executable = (project.findProperty("deployShell") as? String) ?: "sh"

    args(scriptPath, requestedVariant)
    isIgnoreExitValue = true

    onlyIf {
        val exe = executable
        if (exe == null) {
            println("WARNING: No shell executable specified. Skipping deployment.")
            return@onlyIf false
        }
        val shellExists = if (File(exe).isAbsolute) {
            File(exe).exists()
        } else {
            try {
                ProcessBuilder(exe, "--version").start().waitFor() == 0
            } catch (_: Exception) {
                false
            }
        }
        if (!shellExists) {
            println("WARNING: Shell '$exe' not found. Skipping deployment.")
        }
        shellExists
    }

    doFirst {
        println("--- Deployment Info ---")
        println("Variant: $requestedVariant")
        println("VersionName: $verNameValue")
        println("VersionCode: $verCodeValue")
        println("Running deploy.sh from: $scriptPath")
    }

    doLast {
        val result = executionResult.get()
        if (result.exitValue != 0) {
            println("WARNING: deploy.sh failed with exit code ${result.exitValue}")
        } else {
            println("SUCCESS: Deployment completed.")
        }
    }
}

// core.gradle.kts (Kotlin DSL)
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.layout.buildDirectory.get().asFile + "/compose_metrics"
            )
        )
        freeCompilerArgs.addAll(
            listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.layout.buildDirectory.get().asFile + "/compose_metrics"
            )
        )
    }
}

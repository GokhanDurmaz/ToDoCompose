import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.google.services) // The Google Services plugin
}

fun loadProperties(): Properties {
    val properties = Properties()
    System.getenv("VERSION_NAME")?.let { properties.setProperty("VERSION_NAME", it) }
    System.getenv("VERSION_CODE")?.let { properties.setProperty("VERSION_CODE", it) }
    System.getenv("STORE_PASSWORD")?.let { properties.setProperty("STORE_PASSWORD", it) }
    System.getenv("KEY_PASSWORD")?.let { properties.setProperty("KEY_PASSWORD", it) }

    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { inputStream ->
            val localProps = Properties().apply { load(inputStream) }
            localProps.forEach { (key, value) ->
                if (!properties.containsKey(key)) {
                    properties[key] = value
                }
            }
        }
    }
    return properties
}

fun getGitHash(): String {
    return try {
        // Run the git command using ProcessBuilder
        val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
            .directory(File(rootProject.rootDir.path))
            .start()

        val output = process.inputStream.bufferedReader().readText().trim()

        // Wait for the process to finish
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            output
        } else {
            println("Git command failed with exit code $exitCode")
            "unknown"
        }
    } catch (e: Exception) {
        println("Could not run git command: ${e.message}")
        "unknown"
    }
}

fun generateAppName(gitHash: String, variantName: String): String {
    val properties = loadProperties()
    val versionNameFromProps = properties.getProperty("VERSION_NAME") ?: "V1.0.0"
    val versionCodeFromProps = properties.getProperty("VERSION_CODE")?.toIntOrNull() ?: 100
    val sanitizedVersionName = versionNameFromProps.replace(".", "_")
    return "todo_app_${sanitizedVersionName}_${versionCodeFromProps}_${gitHash}_${variantName}.apk"
}

android {
    namespace = "com.flowintent.workspace"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.flowintent.workspace"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        val properties = loadProperties()
        create("release") {
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
            storeFile = file("release-keystore.jks")
            storePassword = properties.getProperty("STORE_PASSWORD")
        }
    }

    applicationVariants.configureEach {
        val variantName = this.name
        outputs.configureEach {
            (this as BaseVariantOutputImpl).outputFileName =
                generateAppName(getGitHash(), variantName)
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
            freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.material3.window.size.class1.android)
    implementation(project(":core"))
    runtimeOnly(project(":data"))

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)
    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    debugImplementation(libs.androidx.ui.test.manifest)
    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary.android)

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation(libs.androidx.material.icons.core)
    // Optional - Add full set of material icons
    implementation(libs.androidx.material.icons.extended)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.accompanist.systemuicontroller)

    // DI tool for compose
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.splashscreen)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation(libs.firebase.analytics)

    // Add the Firebase SDK for Authentication
    implementation(libs.firebase.auth)

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

tasks.register<Exec>("deployApk") {
    val requestedVariant = getRequestedVariant()
    val variant = android.applicationVariants
        .first { it.name.equals(requestedVariant, ignoreCase = true) }
    val apkName = variant.outputs.first().outputFile.name
    println("Variant: $requestedVariant")
    println("APK file name: $apkName")
    println("VersionName: ${loadProperties().getProperty("VERSION_NAME")}")
    println("VersionCode: ${loadProperties().getProperty("VERSION_CODE")}")

    val deployScript = rootProject.file("deploy.sh")
    if (deployScript.exists()) {
        println("Running deploy.sh with $apkName ...")
    } else {
        println("missing: deploy.sh")
    }

    workingDir = rootProject.rootDir
    commandLine("sh", rootProject.file("deploy.sh").absolutePath, requestedVariant)

    doLast {
        if (executionResult.get().exitValue != 0) {
            println("deploy.sh failed but continuing build")
        }
    }
}

// build.gradle.kts (Kotlin DSL)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
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

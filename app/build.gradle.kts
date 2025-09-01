import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { inputStream ->
            properties.load(inputStream)
        }
    }
    return properties
}

fun generateAppName(): String {
    val properties = loadProperties()
    val versionNameFromProps = properties.getProperty("versionName")
    val versionCodeFromProps = properties.getProperty("versionCode").toIntOrNull() ?: 100
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    val sanitizedVersionName = versionNameFromProps.replace(".", "_")
    return "todo_app_${sanitizedVersionName}_${versionCodeFromProps}_${timestamp}.apk"
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    applicationVariants.all {
        outputs.all {
            (this as BaseVariantOutputImpl).outputFileName = generateAppName()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    implementation(libs.material)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.material3.window.size.class1.android)
    implementation(project(":core"))
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)
    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
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
    testImplementation(kotlin("test"))

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

    // Serialization/deserialization json - GSON
    implementation(libs.gson)


    // Kotlin reflection library
    implementation(libs.kotlin.reflect)

}

tasks.named("build") {
    dependsOn(":buildApp")
    finalizedBy("deployApk")
}

tasks.register<Exec>("deployApk") {
    val requestedVariant = project.findProperty("variant")?.toString() ?: "debug"
    val variant = android.applicationVariants
        .first { it.name.equals(requestedVariant, ignoreCase = true) }
    val apkName = variant.outputs.first().outputFile.name
    println("APK file name: $apkName")
    println("VersionName: ${loadProperties().getProperty("versionName")}")
    println("VersionCode: ${loadProperties().getProperty("versionCode")}")

    val deployScript = rootProject.file("deploy.sh")
    if (deployScript.exists()) {
        println("Running deploy.sh with $apkName ...")
    } else {
        println("missing: deploy.sh")
    }

    workingDir = rootProject.rootDir
    commandLine("sh", rootProject.file("deploy.sh").absolutePath)

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

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.android.compose)
    alias(libs.plugins.flowintent.hilt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.flowintent.settings"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(project(":feature:auth"))
    implementation(project(":feature:uikit"))
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.hilt)
    alias(libs.plugins.flowintent.room)
}

android {
    namespace = "com.flowintent.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
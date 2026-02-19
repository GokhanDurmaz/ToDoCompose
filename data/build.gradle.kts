plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.hilt)
    alias(libs.plugins.flowintent.room)
}

android {
    namespace = "com.flowintent.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

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

    implementation(project(":schema"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:profile"))

    // Secure keystore tools
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.tink.android)

    // Serialization/deserialization json - GSON
    implementation(libs.gson)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation(libs.firebase.analytics)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
}

configurations.all {
    resolutionStrategy {
        force("com.google.protobuf:protobuf-javalite:4.33.5")
        force("com.google.firebase:protolite-well-known-types:18.0.1")
    }
}

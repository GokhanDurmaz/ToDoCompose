plugins {
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.flowintent.room)
}

android {
    namespace = "com.flowintent.data"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":schema"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))

    // Secure keystore tools
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.tink.android)

    // Serialization/deserialization json - GSON
    implementation(libs.gson)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.storage.kt)
    implementation(libs.supabase.postgrest.kt)
    implementation(libs.supabase.realtime.kt)

    implementation(libs.androidx.room.paging)

    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.firebase.appcheck.debug)
}

configurations.all {
    resolutionStrategy {
        force("com.google.protobuf:protobuf-javalite:3.25.1")
        force("com.google.firebase:protolite-well-known-types:18.0.0")
    }
}

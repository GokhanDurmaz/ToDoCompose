import com.flowintent.build_logic.secret.loadSecretConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.hilt)
    alias(libs.plugins.flowintent.android.secret)
}

val config = loadSecretConfig()
println("CONFIG DEBUG: ${config["secretKeys"]}")

androidSecrets {
    libName.set(config["libName"]?.toString() ?: "network_native")
    cppFileName.set(config["cppFileName"]?.toString() ?: "native-lib.cpp")
    cmakePath.set(config["cmakePath"]?.toString() ?: "src/main/cpp/CMakeLists.txt")
    cmakeVersion.set(config["cmakeVersion"]?.toString() ?: "3.22.1")

    val keys = (config["secretKeys"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
    secretKeys.addAll(keys)

    val libs = (config["linkedLibraries"] as? List<*>)?.filterIsInstance<String>() ?: listOf("log")
    linkedLibraries.addAll(libs)
}

android {
    namespace = "com.flowintent.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // Serialization/deserialization json - GSON
    implementation(libs.gson)
    implementation(libs.converter.gson)

    implementation(platform(libs.supabase.bom))

    implementation(libs.supabase.storage.kt)
    implementation(libs.supabase.postgrest.kt)
    implementation(libs.supabase.realtime.kt)

    implementation(libs.ktor.client.android)
}
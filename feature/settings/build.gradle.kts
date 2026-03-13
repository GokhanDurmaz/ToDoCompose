plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.android.compose)
    alias(libs.plugins.flowintent.hilt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.flowintent.settings"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(project(":feature:auth"))
    implementation(project(":feature:uikit"))
}
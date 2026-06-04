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
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:uikit"))
    implementation(libs.coil.compose)
}

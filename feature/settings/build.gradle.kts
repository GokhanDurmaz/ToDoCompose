plugins {
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.flowintent.settings"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:uikit"))
    implementation(libs.coil.compose)
}

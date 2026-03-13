plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.hilt)
}

android {
    namespace = "com.flowintent.navigation"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
}
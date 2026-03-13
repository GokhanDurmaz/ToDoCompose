plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.hilt)
}

android {
    namespace = "com.flowintent.profile"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
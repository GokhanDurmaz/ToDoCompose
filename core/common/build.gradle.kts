plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.hilt)
    alias(libs.plugins.flowintent.room)
    alias(libs.plugins.flowintent.android.compose)
}

android {
    namespace = "com.flowintent.core"

    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.paging)
}
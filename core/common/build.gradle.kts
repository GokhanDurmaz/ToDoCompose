plugins {
    alias(libs.plugins.flowintent.room)
    alias(libs.plugins.flowintent.android.compose)
}

android {
    namespace = "com.flowintent.core.common"

    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.paging)
}
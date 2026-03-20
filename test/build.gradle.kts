import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
}

android {
    namespace = "com.flowintent.test"
}

dependencies {
    testImplementation(project(":core:common"))
    testImplementation(project(":data"))
    testImplementation(kotlin("test"))

    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.core)
    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)
    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.room.paging)
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.flowintent.android.base)
    alias(libs.plugins.flowintent.android.compose)
}

android {
    namespace = "com.flowintent.test"
}

val mockitoAgent by configurations.creating

dependencies {
    mockitoAgent(libs.mockito.core) { isTransitive = false }
    testImplementation(project(":core:common"))
    testImplementation(project(":core:navigation"))
    testImplementation(project(":data"))
    testImplementation(kotlin("test"))

    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.core)

    // Feature Dependencies for Screen Tests
    testImplementation(project(":feature:auth"))
    testImplementation(project(":feature:profile"))
    testImplementation(project(":feature:settings"))
    androidTestImplementation(project(":feature:auth"))
    androidTestImplementation(project(":feature:profile"))
    androidTestImplementation(project(":feature:settings"))
    androidTestImplementation(project(":feature:uikit"))
    androidTestImplementation(project(":core:navigation"))

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)
    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.room.paging)
}

tasks.withType<Test>().configureEach {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

plugins {
    alias(libs.plugins.flowintent.android.compose)
    alias(libs.plugins.flowintent.jacoco)
}

android {
    namespace = "com.flowintent.test"

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
}

val mockitoAgent by configurations.creating

dependencies {
    mockitoAgent(libs.mockito.core) { isTransitive = false }
    testImplementation(project(":core:common"))
    testImplementation(project(":core:navigation"))
    testImplementation(project(":data"))
    testImplementation(kotlin("test"))

    androidTestImplementation(project(":core:common"))
    androidTestImplementation(project(":core:navigation"))
    androidTestImplementation(project(":data"))

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.core)

    // Feature Dependencies for Screen Tests
    testImplementation(project(":feature:auth"))
    testImplementation(project(":feature:home"))
    testImplementation(project(":feature:profile"))
    testImplementation(project(":feature:settings"))
    testImplementation(project(":feature:uikit"))
    testImplementation(project(":app"))
    androidTestImplementation(project(":feature:auth"))
    androidTestImplementation(project(":feature:home"))
    androidTestImplementation(project(":feature:profile"))
    androidTestImplementation(project(":feature:settings"))
    androidTestImplementation(project(":feature:uikit"))
    androidTestImplementation(project(":core:navigation"))
    androidTestImplementation(project(":app"))

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)
    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}

tasks.withType<Test>().configureEach {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

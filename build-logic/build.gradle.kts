plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:9.0.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.10")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains:annotations:26.0.2-1")
    }
}

gradlePlugin {
    plugins {
        register("myDetekt") {
            id = "flowintent.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}
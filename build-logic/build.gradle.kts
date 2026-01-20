plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.11.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.5")
}

gradlePlugin {
    plugins {
        register("myDetekt") {
            id = "flowintent.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}
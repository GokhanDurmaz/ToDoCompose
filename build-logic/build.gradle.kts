plugins {
    `kotlin-dsl`
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("projectCatalog")

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.11.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.5")

    compileOnly(libs.findLibrary("hilt.android.gradle.plugin").get())
    compileOnly(libs.findLibrary("com.google.devtools.ksp.gradle.plugin").get())
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains:annotations:23.0.0")
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

gradlePlugin {
    plugins {
        register("hilt") {
            id = "flowintent.hilt"
            implementationClass = "com.flowintent.build_logic.HiltConventionPlugin"
        }
    }
}
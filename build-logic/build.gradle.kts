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
    implementation(libs.findLibrary("gradle").get())
    implementation(libs.findLibrary("kotlin-gradle-plugin").get())
    implementation(libs.findLibrary("detekt-gradle-plugin").get())

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
        register("detekt") {
            id = "flowintent.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}

gradlePlugin {
    plugins {
        register("androidbaseplugin") {
            id = "flowintent.android.base"
            implementationClass = "com.flowintent.build_logic.AndroidBaseConventionPlugin"
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

gradlePlugin {
    plugins {
        register("room") {
            id = "flowintent.room"
            implementationClass = "com.flowintent.build_logic.RoomConventionPlugin"
        }
    }
}
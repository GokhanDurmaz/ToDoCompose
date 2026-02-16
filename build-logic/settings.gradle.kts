dependencyResolutionManagement {
    versionCatalogs {
        create("projectCatalog") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
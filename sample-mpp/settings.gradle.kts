pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("deps") {
            from("dev.sergiobelda.projectconfig.catalog:deps:2026.01.02")
        }
    }
}

includeBuild("..")

rootProject.name = "sample-mpp"

include(":android")
include(":common")
include(":desktop")
include(":web")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

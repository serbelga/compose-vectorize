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
        create("deps") {
            from("dev.sergiobelda.projectconfig.catalog:deps:2026.01.02")
        }
    }
}

rootProject.name = "compose-vectorize"

include(":compose-vectorize-core")
include(":compose-vectorize-gradle-plugin")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
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

rootProject.name = "sample"

include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

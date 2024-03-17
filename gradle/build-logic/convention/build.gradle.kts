plugins {
    `kotlin-dsl`
}

group = "dev.sergiobelda.gradle"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("spotless") {
            id = "dev.sergiobelda.gradle.spotless"
            implementationClass =
                "dev.sergiobelda.gradle.buildlogic.convention.SpotlessConventionPlugin"
        }
    }
}

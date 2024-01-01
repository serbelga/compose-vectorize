plugins {
    `kotlin-dsl`
}

group = "dev.sergiobelda.compose.vectorize.buildlogic"

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
            id = "dev.sergiobelda.compose-vectorize-spotless"
            implementationClass =
                "dev.sergiobelda.compose.vectorize.buildlogic.convention.SpotlessConventionPlugin"
        }
    }
}

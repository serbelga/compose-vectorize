plugins {
    alias(deps.plugins.android.application)
    alias(deps.plugins.jetbrains.kotlin.composeCompiler)
    id("dev.sergiobelda.compose.vectorize")
    alias(deps.plugins.sergiobelda.convention.spotless)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "dev.sergiobelda.compose.vectorize"
    compileSdk = deps.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.sergiobelda.compose.vectorize"
        minSdk = deps.versions.android.minSdk.get().toInt()
        targetSdk = deps.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlin {
        jvmToolchain(libs.versions.jdkPaparazzi.get().toInt())
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core")

    implementation(deps.androidx.activityCompose)
    implementation(deps.androidx.core.ktx)
    implementation(platform(deps.androidx.compose.bom))
    implementation(deps.androidx.compose.ui)
    implementation(deps.androidx.compose.uiGraphics)
    implementation(deps.androidx.compose.uiToolingPreview)
    implementation(deps.androidx.compose.material3)
    debugImplementation(deps.androidx.compose.uiTooling)
}

composeVectorize {
    packageName = "dev.sergiobelda.compose.vectorize.sample.common.images"
}

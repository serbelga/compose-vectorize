plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.sergiobelda.compose-vectorize")
    id("dev.sergiobelda.compose-vectorize-spotless")
}

group = "dev.sergiobelda.compose.vectorize"

android {
    namespace = "dev.sergiobelda.compose.vectorize"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.sergiobelda.compose.vectorize"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
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
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core")

    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.coreKtx)
    implementation(platform(libs.androidx.compose.composeBom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiGraphics)
    implementation(libs.androidx.compose.uiToolingPreview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.uiTooling)
}

composeVectorize {
    packageName = "dev.sergiobelda.compose.vectorize.sample.common.images"
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Copyright 2024 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    kotlin("multiplatform")
    alias(deps.plugins.android.kotlinMultiplatformLibrary)
    alias(deps.plugins.jetbrains.kotlin.composeCompiler)
    alias(deps.plugins.jetbrains.compose)
    id("dev.sergiobelda.compose.vectorize")
    alias(deps.plugins.sergiobelda.convention.spotless)
}

group = "dev.sergiobelda.compose.vectorize.sample.common"

kotlin {
    androidLibrary {
        namespace = "dev.sergiobelda.compose.vectorize.sample.common"
        compileSdk = deps.versions.android.compileSdk.get().toInt()
        minSdk = deps.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "common"
            isStatic = true
        }
    }
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(deps.jetbrains.compose.material3)
            implementation(deps.jetbrains.compose.ui)
            implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core")
        }
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}
composeVectorize {
    packageName = "dev.sergiobelda.compose.vectorize.sample.common.images"
}

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
    `kotlin-dsl`
    alias(libs.plugins.gradlePublish)
    id("dev.sergiobelda.compose-vectorize-spotless")
}

group = "dev.sergiobelda.compose.vectorize"
version = libs.versions.composeVectorize.get()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(projects.composeVectorizeGenerator)
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    website.set("https://github.com/serbelga/compose-vectorize")
    vcsUrl.set("https://github.com/serbelga/compose-vectorize")
    plugins {
        create("compose-vectorize") {
            id = "dev.sergiobelda.compose-vectorize"
            implementationClass =
                "dev.sergiobelda.compose.vectorize.gradle.plugin.ImageVectorGenerationPlugin"
            displayName = "Compose Vectorize"
            description = "Gradle plugin to generate ImageVectors from XML files."
            tags.set(listOf("imagevector", "compose", "xml", "vector"))
        }
    }
}

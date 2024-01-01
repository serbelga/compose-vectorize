# Compose Vectorize

A Kotlin Multiplatform library to generate `compose.ui.graphics.vector.ImageVector` from 
XML files. This library has the same behavior as AndroidX `material-icons` vector generator.

Converts the XML files located in the `xml-images` folder and creates categories based on the folder division.

## Android

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.sergiobelda.compose-vectorize")
}

dependencies {
    implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core:$VERSION")
}

composeVectorize {
    packageName = "dev.example.myproject" // Custom package name
}
```

## Multiplatform

```kotlin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("dev.sergiobelda.compose-vectorize")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core$VERSION")
            }
        }
    }
}

composeVectorize {
    packageName = "dev.example.myproject" // Custom package name
}
```

## License

```
   Copyright 2024 Sergio Belda

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
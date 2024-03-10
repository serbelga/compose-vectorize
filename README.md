# Compose Vectorize

[![Maven Central](https://img.shields.io/maven-central/v/dev.sergiobelda.compose.vectorize/compose-vectorize-core)](https://search.maven.org/search?q=g:dev.sergiobelda.compose.vectorize)

A Kotlin Multiplatform library to generate `compose.ui.graphics.vector.ImageVector` from 
`.xml` files. It is built on top of AndroidX `material-icons` vector generator, but it allows transforming not only solid plain icons but also illustrations and creates categories for resources automatically based on folder structure.

<img src="./docs/assets/diagram.png" alt="Compose Vectorize diagram">

## Themed Attributes

It also supports theme attributes. If you are using theme color attributes, like `?attr/colorPrimary` or `?attr/colorSecondary`, they can be converted to Compose `MaterialTheme` tokens, which means that you can update illustration colors based on the token values. It is also compatible with Material3 dynamic colors.

<div>
    <img width="320" alt="Screenshot_20240310_145842" src="https://github.com/serbelga/compose-vectorize/assets/26246782/73ebdb93-fbaf-454f-aaa2-bf17d9d2dff1">
    <img width="320" alt="Screenshot_20240310_145842-2" src="https://github.com/serbelga/compose-vectorize/assets/26246782/b282aff3-599b-4039-9455-247eed7befc2">
    <img width="320" alt="Screenshot_20240310_145842-1" src="https://github.com/serbelga/compose-vectorize/assets/26246782/e2a10a2e-1eb4-40af-be1e-aeaac1954f23">
</div>

> [!NOTE]  
> It requires use Compose Material 3 dependency.

## How to Use

- [Android](#android)
- [Multiplatform](#multiplatform)
- [Generate images](#generate-images)

### Android

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.sergiobelda.compose.vectorize") version "$VERSION"
}

dependencies {
    implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core:$VERSION")
}

composeVectorize {
    packageName = "dev.example.myproject" // Custom package name
}
```

Create a folder called `xml-images` in the module folder. For example, if you're using this plugin in your application module called `app`, you need to create a folder inside the app folder.

`app/xml-images`

<img width="332" alt="Captura de pantalla 2024-01-05 a las 16 57 56" src="https://github.com/serbelga/compose-vectorize/assets/26246782/97c4082a-28d8-4009-addd-427d9f893340">

### Multiplatform

```kotlin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("dev.sergiobelda.compose.vectorize") version "$VERSION"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation("dev.sergiobelda.compose.vectorize:compose-vectorize-core:$VERSION")
            }
        }
    }
}

composeVectorize {
    packageName = "dev.example.myproject" // Custom package name
}
```

Create a folder called `xml-images` in the module folder. For example:

`common/xml-images`

<img width="330" alt="Captura de pantalla 2024-01-05 a las 17 10 28" src="https://github.com/serbelga/compose-vectorize/assets/26246782/2c0a380d-2580-4898-b581-560e4b7c6e6b">

### Generate images

You can manually generate ImageVector for these XML files by calling `gradle generateImages`. This will create a Kotlin file for each XML file containing the ImageVector in the build folder.

<img width="429" alt="Captura de pantalla 2024-01-05 a las 17 00 45" src="https://github.com/serbelga/compose-vectorize/assets/26246782/45ed3cd3-5773-4cf7-9474-4ef6a30a6476">
<br></br>

Now, you can use reference this image in the Compose code:

```kotlin
Icon(Images.Icons.Add, contentDescription = null)
```

Note that automatically a category called "Icons" has been created. A category is created for each subfolder in `xml-images` folder.

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

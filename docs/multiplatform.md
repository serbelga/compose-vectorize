## How to Use

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

You can manually generate ImageVector for these XML files by calling `gradle generateImages`. This will create a Kotlin file for each XML file containing the ImageVector in the build folder.

<img width="418" alt="Captura de pantalla 2024-01-05 a las 17 12 08" src="https://github.com/serbelga/compose-vectorize/assets/26246782/929752f1-e859-46a8-bacc-d75854952983">
<br></br>

Now, you can use reference this image in the Compose code:

```kotlin
Icon(Images.Icons.Add, contentDescription = null)
```

Note that automatically a category called "Icons" has been created. A category is created for each subfolder in `xml-images` folder.

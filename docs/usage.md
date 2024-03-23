# Usage

Create a folder called `xml-images` in the module folder. 

## Android

For example, if you're using this plugin in your application module called `app`, you need to create a folder inside the app folder.

`app/xml-images`

<img width="332" alt="Captura de pantalla 2024-01-05 a las 16 57 56" src="https://github.com/serbelga/compose-vectorize/assets/26246782/97c4082a-28d8-4009-addd-427d9f893340">

## Multiplatform

Create a folder called `xml-images` in the module folder. For example:

`common/xml-images`

<img width="330" alt="Captura de pantalla 2024-01-05 a las 17 10 28" src="https://github.com/serbelga/compose-vectorize/assets/26246782/2c0a380d-2580-4898-b581-560e4b7c6e6b">

## Generate images

You can manually generate ImageVector for these XML files by calling `gradle generateImages`. This will create a Kotlin file for each XML file containing the ImageVector in the build folder.

<img width="429" alt="Captura de pantalla 2024-01-05 a las 17 00 45" src="https://github.com/serbelga/compose-vectorize/assets/26246782/45ed3cd3-5773-4cf7-9474-4ef6a30a6476">
<br></br>

Now, you can use reference this image in the Compose code:

```kotlin
Icon(Images.Icons.Add, contentDescription = null)
```

Note that automatically a category called "Icons" has been created. A category is created for each subfolder in `xml-images` folder.

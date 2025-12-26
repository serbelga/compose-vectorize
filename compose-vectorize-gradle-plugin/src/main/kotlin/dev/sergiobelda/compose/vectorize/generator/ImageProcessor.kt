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

package dev.sergiobelda.compose.vectorize.generator

import dev.sergiobelda.compose.vectorize.generator.utils.toKotlinPropertyName
import java.io.File
import java.util.Locale

class ImageProcessor(
    private val packageName: String,
    private val imagesDirectories: List<File>,
) {
    fun process(): List<Image> = loadImages()

    private fun loadImages(): List<Image> {
        val images = mutableListOf<Image>()
        imagesDirectories.forEach { file ->
            images.addImages(file)
        }

        // Ensure image names are unique when accounting for case insensitive filesystems
        images
            .groupBy { it.categoryName.plus(it.kotlinName.lowercase(Locale.ROOT)) }
            .filter { it.value.size > 1 }
            .forEach { entry ->
                throw IllegalStateException(
                    """Found multiple images with the same case-insensitive filename:
                        | ${entry.value.firstOrNull()?.xmlFileName}. Generating images with the same
                        | case-insensitive filename will cause issues on devices without
                        | a case sensitive filesystem (OSX / Windows).
                    """.trimMargin(),
                )
            }
        return images
    }

    private fun MutableList<Image>.addImages(
        file: File,
        categoryPath: String = "",
    ) {
        if (file.isDirectory) {
            val category = categoryPath.plus("${file.name.toKotlinPropertyName()}.")
            file.listFiles()?.forEach { child ->
                addImages(child, category)
            }
        } else {
            val filename = file.nameWithoutExtension
            val kotlinName = filename.toKotlinPropertyName()
            val fileContent = file.readText()
            add(
                Image(
                    kotlinName = kotlinName,
                    packageName = packageName,
                    categoryName = categoryPath.toKotlinPropertyName(),
                    xmlFileName = filename,
                    fileContent = processXmlFile(fileContent),
                ),
            )
        }
    }
}

/**
 * Processes the given [fileContent] by removing android theme attributes and values.
 */
private fun processXmlFile(fileContent: String): String {
    // Remove any defined tint for paths that use theme attributes
    val tintAttribute = Regex.escape("""android:tint="?attr/colorControlNormal"""")
    val tintRegex = """\n.*?$tintAttribute""".toRegex(RegexOption.MULTILINE)

    return fileContent
        .replace(tintRegex, "")
        // The imported images have white as the default path color, so let's change it to be
        // black as is typical on Android.
        .replace("@android:color/white", "#FF000000")
}

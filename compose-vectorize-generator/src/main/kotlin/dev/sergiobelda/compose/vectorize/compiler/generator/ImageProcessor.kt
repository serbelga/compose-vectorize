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

package dev.sergiobelda.compose.vectorize.compiler.generator

import dev.sergiobelda.compose.vectorize.compiler.generator.utils.toKotlinPropertyName
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
            file.getImages(
                images,
                ""
            )
        }

        // Ensure image names are unique when accounting for case insensitive filesystems
        images
            .groupBy { it.kotlinName.lowercase(Locale.ROOT) }
            .filter { it.value.size > 1 }
            .forEach { entry ->
                throw IllegalStateException(
                    """Found multiple images with the same case-insensitive filename:
                        | ${entry.value.joinToString()}. Generating images with the same
                        | case-insensitive filename will cause issues on devices without
                        | a case sensitive filesystem (OSX / Windows).
                        """.trimMargin(),
                )
            }
        return images
    }

    private fun File.getImages(
        images: MutableList<Image>,
        categoryPath: String,
    ) {
        if (isDirectory) {
            val c = categoryPath.plus(".${name.toKotlinPropertyName()}")
            listFiles()?.forEach { child ->
                child.getImages(images, c)
            }
        } else {
            val filename = nameWithoutExtension
            val kotlinName = filename.toKotlinPropertyName()
            val fileContent = readText()
            images.add(
                Image(
                    kotlinName = kotlinName,
                    packageName = packageName,
                    categoryName = categoryPath.toKotlinPropertyName(),
                    xmlFileName = filename,
                    fileContent = processXmlFile(fileContent),
                )
            )
        }
    }

    /*private fun loadImages(): List<Image> =
        imagesDirectories.flatMap { dir ->
            val images = dir.walk().filter { !it.isDirectory }.toList()

            val transformedImages = images.map { file ->
                val filename = file.nameWithoutExtension
                val kotlinName = filename.toKotlinPropertyName()

                val fileContent = file.readText()
                Image(
                    kotlinName = kotlinName,
                    packageName = packageName,
                    categoryName = dir.name.toKotlinPropertyName(),
                    xmlFileName = filename,
                    fileContent = processXmlFile(fileContent),
                )
            }

            // Ensure image names are unique when accounting for case insensitive filesystems -
            // workaround for b/216295020
            transformedImages
                .groupBy { it.kotlinName.lowercase(Locale.ROOT) }
                .filter { it.value.size > 1 }
                .forEach { entry ->
                    throw IllegalStateException(
                        """Found multiple images with the same case-insensitive filename:
                        | ${entry.value.joinToString()}. Generating images with the same
                        | case-insensitive filename will cause issues on devices without
                        | a case sensitive filesystem (OSX / Windows).
                        """.trimMargin(),
                    )
                }

            transformedImages
        }*/
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

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

import dev.sergiobelda.compose.vectorize.generator.imageparser.ImageParser
import java.io.File

/**
 * Generates programmatic representation of all [images] using [ImageVectorGenerator].
 *
 * @property images the list of [Image]s to generate Kotlin files for
 */
class ImageWriter(private val images: List<Image>) {
    /**
     * Generates images and writes them to [outputSrcDirectory].
     *
     * @param outputSrcDirectory the directory to generate source files in
     */
    fun generateTo(
        outputSrcDirectory: File,
    ) {
        images.forEach { image ->
            if (image.fileContent.isNotEmpty()) {
                val vector = ImageParser(image).parse()

                val fileSpec = ImageVectorGenerator(
                    image.kotlinName,
                    image.packageName,
                    image.categoryName,
                    vector,
                ).createFileSpec()

                fileSpec.writeTo(outputSrcDirectory)
            }
        }
    }
}

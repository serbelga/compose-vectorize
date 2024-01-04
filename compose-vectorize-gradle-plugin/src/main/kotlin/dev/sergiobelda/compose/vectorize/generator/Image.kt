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

/**
 * Represents a image's Kotlin name, image category name, processed XML file name, and XML file content.
 *
 * The [kotlinName] is typically the PascalCase equivalent of the original image name, with the
 * caveat that images starting with a number are prefixed with an underscore.
 *
 * @property kotlinName the name of the generated Kotlin property, for example `ArrowBack`.
 * @property categoryName the image category name, for example `Icons`.
 * @property xmlFileName the name of the processed XML file.
 * @property fileContent the content of the source XML file that will be parsed.
 */
data class Image(
    val kotlinName: String,
    val packageName: String,
    val categoryName: String,
    val xmlFileName: String,
    val fileContent: String,
)

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

package dev.sergiobelda.compose.vectorize.generator.task

import dev.sergiobelda.compose.vectorize.generator.Image
import dev.sergiobelda.compose.vectorize.generator.ImageCategories
import dev.sergiobelda.compose.vectorize.generator.ImageCategoriesProcessor
import dev.sergiobelda.compose.vectorize.generator.ImageCategoriesWriter
import dev.sergiobelda.compose.vectorize.generator.ImageProcessor
import dev.sergiobelda.compose.vectorize.generator.ImageWriter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class ImageVectorGenerationTask : DefaultTask() {
    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFiles
    val imagesDirectory = project.projectDir.resolve("xml-images")

    @Internal
    fun getImageDirectories(): List<File> = imagesDirectory.listFiles()?.filter { it.isDirectory } ?: emptyList()

    @OutputDirectory
    lateinit var buildDirectory: File

    @Internal
    lateinit var packageName: String

    @Internal
    lateinit var generatedSrcMain: String

    private fun loadImageCategories(): ImageCategories =
        ImageCategoriesProcessor(
            packageName,
            getImageDirectories(),
        ).process()

    private fun loadImages(): List<Image> =
        ImageProcessor(
            packageName,
            getImageDirectories(),
        ).process()

    @get:OutputDirectory
    val generatedSrcMainDirectory: File
        get() = buildDirectory.resolve(generatedSrcMain)

    @TaskAction
    fun run() {
        ImageCategoriesWriter(loadImageCategories()).generateTo(generatedSrcMainDirectory)
        ImageWriter(loadImages()).generateTo(generatedSrcMainDirectory)
    }
}

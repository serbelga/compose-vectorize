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

package dev.sergiobelda.compose.vectorize.gradle.plugin

import com.android.build.gradle.BaseExtension
import dev.sergiobelda.compose.vectorize.generator.task.ImageVectorGenerationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

interface ImageVectorGenerationPluginExtension {
    val packageName: Property<String>
}

open class ImageVectorGenerationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create<ImageVectorGenerationPluginExtension>(EXTENSION_PLUGIN_NAME)

        val buildDirectory =
            project.layout.buildDirectory.asFile.get().resolve(IMAGES_RELATIVE_PATH)

        val generatedSrcMain = project.generatedSrcMain()

        val task =
            project.registerImageVectorGenerationTask(extension, buildDirectory, generatedSrcMain)

        if (project.isMultiplatform) {
            val sourceSet = project.multiplatformExtension?.sourceSets?.find {
                it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME
            }
            val generatedSrcMainDirectory = buildDirectory.resolve(generatedSrcMain)
            sourceSet?.kotlin?.srcDir(project.files(generatedSrcMainDirectory).builtBy(task))
        } else {
            project.tasks
                .matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }
                .configureEach { dependsOn(task) }

            val sourceSet = project.baseExtension?.sourceSets?.find {
                it.name == SourceSet.MAIN_SOURCE_SET_NAME
            }
            val generatedSrcMainDirectory = buildDirectory.resolve(generatedSrcMain)
            sourceSet?.kotlin?.srcDir(project.files(generatedSrcMainDirectory).builtBy(task))
        }
    }

    private fun Project.registerImageVectorGenerationTask(
        extension: ImageVectorGenerationPluginExtension,
        buildDirectory: File,
        generatedSrcMain: String,
    ) = tasks.register(TASK_NAME, ImageVectorGenerationTask::class.java) {
        this.packageName = extension.packageName.getOrElse(DEFAULT_PACKAGE_NAME)
        this.buildDirectory = buildDirectory
        this.generatedSrcMain = generatedSrcMain
    }

    private companion object {
        const val DEFAULT_PACKAGE_NAME = "dev.sergiobelda.compose.vectorize.images"
        const val EXTENSION_PLUGIN_NAME = "composeVectorize"
        const val IMAGES_RELATIVE_PATH = "images"
        const val TASK_NAME = "generateImages"
    }
}

private val Project.isMultiplatform: Boolean
    get() = plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

private fun Project.generatedSrcMain(): String =
    if (isMultiplatform) {
        "src/commonMain/kotlin"
    } else {
        "src/main/kotlin"
    }

private val Project.baseExtension
    get() = extensions.findByType(BaseExtension::class.java)

private val Project.multiplatformExtension
    get() = extensions.findByType(KotlinMultiplatformExtension::class.java)

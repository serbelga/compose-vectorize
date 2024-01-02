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

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import dev.sergiobelda.compose.vectorize.compiler.generator.utils.toKotlinPropertyName
import java.io.File

class ImageCategoriesGenerator(
    private val imageCategoriesPackageName: String,
    private val categories: List<File>,
) {

    fun createFileSpec(): FileSpec {
        val builder = createImagesFileSpecBuilder()
        val imagesType = TypeSpec.objectBuilder(Images)
        imagesType.addImagesCategoriesTypeSpec(
            categories
        )
        builder.addType(imagesType.build())
        return builder.build()
    }

    private fun createImagesFileSpecBuilder(): FileSpec.Builder {
        return FileSpec.builder(
            packageName = imageCategoriesPackageName,
            fileName = Images,
        )
    }

    // TODO: Rename
    private fun TypeSpec.Builder.addImagesCategoriesTypeSpec(files: List<File>): TypeSpec.Builder {
        files.forEach { file ->
            if (file.isDirectory) {
                val type = TypeSpec.objectBuilder(file.name.toKotlinPropertyName())
                type.addImagesCategoriesTypeSpec(
                    file.listFiles()?.toList() ?: emptyList()
                )
                addType(type.build())
            }
        }
        return this
    }

    private companion object {
        // TODO: Make Images string dynamic.
        const val Images = "Images"
    }
}

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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import dev.sergiobelda.compose.vectorize.generator.utils.setIndent
import dev.sergiobelda.compose.vectorize.generator.vector.FillType
import dev.sergiobelda.compose.vectorize.generator.vector.StrokeCap
import dev.sergiobelda.compose.vectorize.generator.vector.Vector
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode
import java.util.Locale

/**
 * Generator for creating a Kotlin source file with an ImageVector property for the given [vector],
 * with name [imageName].
 *
 * @param imageName the name for the generated property, which is also used for the generated file.
 * I.e if the name is `Menu`, the property will be `Menu` (inside a theme receiver object) and
 * the file will be `Menu.kt` (under the theme package name).
 * @param vector the parsed vector to generate ImageVector.Builder commands for
 */
class ImageVectorGenerator(
    private val imageName: String,
    private val imagePackageName: String,
    private val imageCategoryName: String,
    private val vector: Vector,
) {
    /**
     * @return a [FileSpec] representing a Kotlin source file containing the property for this
     * programmatic [vector] representation.
     *
     * The package name and hence file location of the generated file is: [imagePackageName].
     */
    fun createFileSpec(): FileSpec {
        val builder = createFileSpecBuilder()
        val backingProperty = getBackingProperty()
        val propertySpecBuilder =
            PropertySpec.builder(name = imageName, type = ClassNames.ImageVector)
                .receiver(
                    ClassName(
                        imagePackageName,
                        "Images",
                        imageCategoryName,
                    ),
                )
                .getter(
                    imageGetter(
                        backingProperty = backingProperty,
                        imageName = imageName,
                    ),
                )
        builder.addProperty(propertySpecBuilder.build())
        builder.addProperty(backingProperty)
        return builder.setIndent().build()
    }

    private fun createFileSpecBuilder(): FileSpec.Builder {
        val imagesPackage =
            imagePackageName
                .plus(".")
                .plus(imageCategoryName.lowercase(Locale.ROOT))
        return FileSpec.builder(
            packageName = imagesPackage,
            fileName = imageName,
        )
    }

    private fun getBackingProperty(): PropertySpec {
        val backingPropertyName = "_" + imageName.replaceFirstChar { it.lowercase(Locale.ROOT) }
        return backingProperty(name = backingPropertyName)
    }

    private fun imageGetter(
        backingProperty: PropertySpec,
        imageName: String,
    ): FunSpec {
        return FunSpec.getterBuilder()
            .addCode(
                buildCodeBlock {
                    beginControlFlow("if (%N != null)", backingProperty)
                    addStatement("return %N!!", backingProperty)
                    endControlFlow()
                },
            )
            .addCode(
                buildCodeBlock {
                    val parameterList = listOfNotNull(
                        "name = \"%N\"",
                        "width = ${vector.width}f",
                        "height = ${vector.height}f",
                        "viewportWidth = ${vector.viewportWidth}f",
                        "viewportHeight = ${vector.viewportHeight}f",
                    )
                    val parameters = if (parameterList.isNotEmpty()) {
                        parameterList.joinToString(
                            prefix = "%N = %M(\n\t",
                            postfix = "\n)",
                            separator = ",\n\t",
                        )
                    } else {
                        ""
                    }
                    beginControlFlow(
                        parameters,
                        backingProperty,
                        MemberNames.ImageVector,
                        imageName,
                    )
                    vector.nodes.forEach { node -> addRecursively(node) }
                    endControlFlow()
                },
            )
            .addStatement("return %N!!", backingProperty)
            .build()
    }

    private fun backingProperty(name: String): PropertySpec {
        val nullableImageVector = ClassNames.ImageVector.copy(nullable = true)
        return PropertySpec.builder(name = name, type = nullableImageVector)
            .mutable()
            .addModifiers(KModifier.PRIVATE)
            .initializer("null")
            .build()
    }
}

/**
 * Recursively adds function calls to construct the given [vectorNode] and its children.
 */
private fun CodeBlock.Builder.addRecursively(vectorNode: VectorNode) {
    when (vectorNode) {
        is VectorNode.Group -> {
            beginControlFlow("%M", MemberNames.Group)
            vectorNode.paths.forEach { path ->
                addRecursively(path)
            }
            endControlFlow()
        }

        is VectorNode.Path -> {
            addPath(vectorNode) {
                vectorNode.nodes.forEach { pathNode ->
                    addStatement(pathNode.asFunctionCall())
                }
            }
        }
    }
}

/**
 * Adds a function call to create the given [path], with [pathBody] containing the commands for
 * the path.
 */
private fun CodeBlock.Builder.addPath(
    path: VectorNode.Path,
    pathBody: CodeBlock.Builder.() -> Unit,
) {
    val parameterList = mutableListOf<String>()
    val memberList = mutableListOf<MemberName>()

    with(path) {
        memberList.add(MemberNames.Path)

        parameterList.add("fill = %M(%M(${fillColor.replace("#", "0x")}))")
        memberList.add(MemberNames.SolidColor)
        memberList.add(MemberNames.Color)
        fillAlpha.takeIf { it != 1f }?.let {
            parameterList.add("fillAlpha = ${fillAlpha}f")
        }
        fillType.takeIf { it != FillType.NonZero }?.let {
            parameterList.add("pathFillType = %M")
            memberList.add(MemberNames.EvenOdd)
        }
        strokeAlpha.takeIf { it != 1f }?.let {
            parameterList.add("strokeAlpha = ${strokeAlpha}f")
        }
        strokeColor.takeIf { it.isNotEmpty() }?.let {
            parameterList.add("stroke = %M(%M(${strokeColor.replace("#", "0x")}))")
            memberList.add(MemberNames.SolidColor)
            memberList.add(MemberNames.Color)
        }
        strokeCap.takeIf { it != StrokeCap.Butt }?.let {
            parameterList.add("strokeLineCap = %M")
            when (strokeCap) {
                StrokeCap.Round -> memberList.add(MemberNames.Round)
                StrokeCap.Square -> memberList.add(MemberNames.Square)
                else -> memberList.add(MemberNames.Butt)
            }
        }
        strokeWidth.takeIf { it != 0f }?.let {
            parameterList.add("strokeLineWidth = ${strokeWidth}f")
        }
    }
    addPathParameters(parameterList, memberList)
    pathBody()
    endControlFlow()
}

private fun CodeBlock.Builder.addPathParameters(
    parameterList: List<String>,
    memberList: List<MemberName>,
) {
    val parameters = if (parameterList.isNotEmpty()) {
        parameterList.joinToString(
            prefix = "(\n\t",
            postfix = "\n)",
            separator = ",\n\t",
        )
    } else {
        ""
    }

    beginControlFlow("%M$parameters", *memberList.toTypedArray())
}

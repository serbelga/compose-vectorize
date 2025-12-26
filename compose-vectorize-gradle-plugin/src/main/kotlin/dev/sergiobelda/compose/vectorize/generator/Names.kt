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
import com.squareup.kotlinpoet.MemberName

/**
 * Package names used for image generation.
 */
enum class PackageNames(
    val packageName: String,
) {
    ComposeVectorizeCore("dev.sergiobelda.compose.vectorize.core"),
    GraphicsPackage("androidx.compose.ui.graphics"),
    Material3Package("androidx.compose.material3"),
    VectorPackage(GraphicsPackage.packageName + ".vector"),
}

/**
 * [ClassName]s used for image generation.
 */
object ClassNames {
    val ImageVector = PackageNames.VectorPackage.className("ImageVector")

    val PathFillType = PackageNames.GraphicsPackage.className("PathFillType", "Companion")
    val StrokeCapType = PackageNames.GraphicsPackage.className("StrokeCap", "Companion")
    val StrokeJoinType = PackageNames.GraphicsPackage.className("StrokeJoin", "Companion")

    val MaterialTheme = PackageNames.Material3Package.className("MaterialTheme")
}

/**
 * [ClassName]s used as Annotation for image generation.
 */
object AnnotationNames {
    val Composable = ClassName("androidx.compose.runtime", "Composable")
}

/**
 * [MemberName]s used for image generation.
 */
object MemberNames {
    val Material3ColorScheme = MemberName(ClassNames.MaterialTheme, "colorScheme")

    val ImageVector =
        MemberName(
            PackageNames.ComposeVectorizeCore.packageName,
            "imageVector",
        )

    val Color = MemberName(PackageNames.GraphicsPackage.packageName, "Color")
    val SolidColor = MemberName(PackageNames.GraphicsPackage.packageName, "SolidColor")

    val Group = MemberName(PackageNames.VectorPackage.packageName, "group")
    val Path = MemberName(PackageNames.VectorPackage.packageName, "path")

    object PathFillType {
        val EvenOdd = MemberName(ClassNames.PathFillType, "EvenOdd")
    }

    object StrokeCapType {
        val Butt = MemberName(ClassNames.StrokeCapType, "Butt")
        val Round = MemberName(ClassNames.StrokeCapType, "Round")
        val Square = MemberName(ClassNames.StrokeCapType, "Square")
    }

    object StrokeJoinType {
        val Bevel = MemberName(ClassNames.StrokeJoinType, "Bevel")
        val Miter = MemberName(ClassNames.StrokeJoinType, "Miter")
        val Round = MemberName(ClassNames.StrokeJoinType, "Round")
    }
}

/**
 * @return the [ClassName] of the given [classNames] inside this package.
 */
fun PackageNames.className(vararg classNames: String) = ClassName(this.packageName, *classNames)

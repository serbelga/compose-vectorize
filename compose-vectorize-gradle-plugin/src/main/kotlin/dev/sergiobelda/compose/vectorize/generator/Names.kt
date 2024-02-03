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
enum class PackageNames(val packageName: String) {
    ComposeVectorizeCore("dev.sergiobelda.compose.vectorize.core"),
    GraphicsPackage("androidx.compose.ui.graphics"),
    VectorPackage(GraphicsPackage.packageName + ".vector"),
}

/**
 * [ClassName]s used for image generation.
 */
object ClassNames {
    val ImageVector = PackageNames.VectorPackage.className("ImageVector")
    val PathFillType = PackageNames.GraphicsPackage.className("PathFillType", "Companion")
    val StrokeCapType = PackageNames.GraphicsPackage.className("StrokeCap", "Companion")
}

/**
 * [MemberName]s used for image generation.
 */
object MemberNames {
    val Butt = MemberName(ClassNames.StrokeCapType, "Butt")
    val Color = MemberName(PackageNames.GraphicsPackage.packageName, "Color")
    val EvenOdd = MemberName(ClassNames.PathFillType, "EvenOdd")
    val Group = MemberName(PackageNames.VectorPackage.packageName, "group")
    val ImageVector = MemberName(
        PackageNames.ComposeVectorizeCore.packageName,
        "imageVector",
    )
    val Path = MemberName(PackageNames.VectorPackage.packageName, "path")
    val Round = MemberName(ClassNames.StrokeCapType, "Round")
    val SolidColor = MemberName(PackageNames.GraphicsPackage.packageName, "SolidColor")
    val Square = MemberName(ClassNames.StrokeCapType, "Square")
}

/**
 * @return the [ClassName] of the given [classNames] inside this package.
 */
fun PackageNames.className(vararg classNames: String) = ClassName(this.packageName, *classNames)

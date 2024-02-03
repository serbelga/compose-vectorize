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

package dev.sergiobelda.compose.vectorize.generator.vector

/**
 * Simplified representation of a vector, with root [nodes].
 *
 * @param nodes may either be a singleton list of the root group, or a list of root paths / groups
 * if there are multiple top level declaration
 */
class Vector(
    val width: String,
    val height: String,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val nodes: List<VectorNode>,
)

sealed class VectorNode {
    class Group(val paths: MutableList<Path> = mutableListOf()) : VectorNode()
    class Path(
        val fillAlpha: Float,
        val fillColor: String,
        val fillType: FillType,
        val nodes: List<PathNode>,
        val strokeAlpha: Float,
        val strokeCap: StrokeCap,
        val strokeColor: String,
        val strokeWidth: Float,
    ) : VectorNode()
}

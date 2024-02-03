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

import dev.sergiobelda.compose.vectorize.generator.vector.FillType
import dev.sergiobelda.compose.vectorize.generator.vector.PathParser
import dev.sergiobelda.compose.vectorize.generator.vector.StrokeCap
import dev.sergiobelda.compose.vectorize.generator.vector.Vector
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_DOCUMENT
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

/**
 * Parser that converts [image]s into [Vector]s
 */
class ImageParser(private val image: Image) {

    /**
     * @return a [Vector] representing the provided [image].
     */
    fun parse(): Vector {
        val parser = XmlPullParserFactory.newInstance().newPullParser().apply {
            setInput(image.fileContent.byteInputStream(), null)
            seekToStartTag()
        }

        check(parser.name == VECTOR) { "The start tag must be <vector>!" }

        var width = ""
        var height = ""
        var viewportWidth = 0f
        var viewportHeight = 0f
        val nodes = mutableListOf<VectorNode>()

        var currentGroup: VectorNode.Group? = null

        while (!parser.isAtEnd()) {
            when (parser.eventType) {
                START_TAG -> {
                    when (parser.name) {
                        VECTOR -> {
                            width = parser.getValueAsString(WIDTH).processDpDimension()
                            height = parser.getValueAsString(HEIGHT).processDpDimension()
                            viewportWidth = parser.getValueAsFloat(VIEWPORT_WIDTH) ?: 0f
                            viewportHeight = parser.getValueAsFloat(VIEWPORT_HEIGHT) ?: 0f
                        }

                        PATH -> {
                            val pathData = parser.getAttributeValue(
                                null,
                                PATH_DATA,
                            )
                            val fillAlpha = parser.getValueAsFloat(FILL_ALPHA)
                            val fillColor = parser.getValueAsString(FILL_COLOR).processColor()
                            val fillType = when (parser.getAttributeValue(null, FILL_TYPE)) {
                                // evenOdd and nonZero are the only supported values here, where
                                // nonZero is the default if no values are defined.
                                EVEN_ODD -> FillType.EvenOdd
                                else -> FillType.NonZero
                            }
                            val strokeAlpha = parser.getValueAsFloat(STROKE_ALPHA)
                            val strokeCap = when (parser.getAttributeValue(null, "android:strokeLineCap")) {
                                BUTT -> StrokeCap.Butt
                                ROUND -> StrokeCap.Round
                                SQUARE -> StrokeCap.Square
                                else -> StrokeCap.Butt
                            }
                            val strokeColor = parser.getValueAsString(STROKE_COLOR).processColor()
                            val strokeWidth = parser.getValueAsFloat(STROKE_WIDTH)

                            val path = VectorNode.Path(
                                fillAlpha = fillAlpha ?: 1f,
                                fillColor = fillColor.uppercase(),
                                fillType = fillType,
                                nodes = PathParser.parsePathString(pathData),
                                strokeAlpha = strokeAlpha ?: 1f,
                                strokeCap = strokeCap,
                                strokeColor = strokeColor,
                                strokeWidth = strokeWidth ?: 0f,
                            )
                            if (currentGroup != null) {
                                currentGroup.paths.add(path)
                            } else {
                                nodes.add(path)
                            }
                        }

                        GROUP -> {
                            val group = VectorNode.Group()
                            currentGroup = group
                            nodes.add(group)
                        }

                        CLIP_PATH -> {
                        }
                    }
                }
            }
            parser.next()
        }

        return Vector(
            width = width,
            height = height,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            nodes = nodes,
        )
    }
}

/**
 * @return the float value for the attribute [name], or null if it couldn't be found
 */
private fun XmlPullParser.getValueAsFloat(name: String): Float? =
    getAttributeValue(null, name)?.toFloatOrNull()

/**
 * @return the string value for the attribute [name], or null if it couldn't be found
 */
private fun XmlPullParser.getValueAsString(name: String): String =
    getAttributeValue(null, name)?.toString() ?: ""

private fun XmlPullParser.seekToStartTag(): XmlPullParser {
    var type = next()
    while (type != START_TAG && type != END_DOCUMENT) {
        // Empty loop
        type = next()
    }
    if (type != START_TAG) {
        throw XmlPullParserException("No start tag found")
    }
    return this
}

private fun XmlPullParser.isAtEnd() =
    eventType == END_DOCUMENT || (depth < 1 && eventType == END_TAG)

private fun String.processDpDimension(): String =
    this.replace("dp", "")

private fun String.processColor(): String {
    val diff = ARGB_HEXADECIMAL_COLOR_LENGTH - this.length
    return if (diff > 0) {
        this.replace("#", "#${"F".repeat(diff)}")
    } else {
        this
    }
}

private const val ARGB_HEXADECIMAL_COLOR_LENGTH = 9

// XML tag names
private const val VECTOR = "vector"
private const val CLIP_PATH = "clip-path"
private const val GROUP = "group"
private const val PATH = "path"

// XML attribute names
private const val FILL_ALPHA = "android:fillAlpha"
private const val FILL_COLOR = "android:fillColor"
private const val FILL_TYPE = "android:fillType"
private const val HEIGHT = "android:height"
private const val PATH_DATA = "android:pathData"
private const val STROKE_ALPHA = "android:strokeAlpha"
private const val STROKE_COLOR = "android:strokeColor"
private const val STROKE_WIDTH = "android:strokeWidth"
private const val VIEWPORT_HEIGHT = "android:viewportHeight"
private const val VIEWPORT_WIDTH = "android:viewportWidth"
private const val WIDTH = "android:width"

// XML attribute values
private const val BUTT = "butt"
private const val EVEN_ODD = "evenOdd"
private const val ROUND = "round"
private const val SQUARE = "square"

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

package dev.sergiobelda.compose.vectorize.generator.imageparser

import dev.sergiobelda.compose.vectorize.generator.Image
import dev.sergiobelda.compose.vectorize.generator.vector.FillType
import dev.sergiobelda.compose.vectorize.generator.vector.PathParser
import dev.sergiobelda.compose.vectorize.generator.vector.StrokeCap
import dev.sergiobelda.compose.vectorize.generator.vector.StrokeJoin
import dev.sergiobelda.compose.vectorize.generator.vector.Vector
import dev.sergiobelda.compose.vectorize.generator.vector.Vector.Companion.DefaultAutoMirror
import dev.sergiobelda.compose.vectorize.generator.vector.Vector.Companion.DefaultHeight
import dev.sergiobelda.compose.vectorize.generator.vector.Vector.Companion.DefaultViewportHeight
import dev.sergiobelda.compose.vectorize.generator.vector.Vector.Companion.DefaultViewportWidth
import dev.sergiobelda.compose.vectorize.generator.vector.Vector.Companion.DefaultWidth
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultFillAlpha
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultFillType
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultStrokeAlpha
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultStrokeCap
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultStrokeLineJoin
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultStrokeLineMiter
import dev.sergiobelda.compose.vectorize.generator.vector.VectorNode.Path.Companion.DefaultStrokeWidth
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_DOCUMENT
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

/**
 * Parser that converts [image]s into [Vector]s
 */
class ImageParser(
    private val image: Image,
) {
    /**
     * @return a [Vector] representing the provided [image].
     */
    fun parse(): Vector {
        val parser =
            XmlPullParserFactory.newInstance().newPullParser().apply {
                setInput(image.fileContent.byteInputStream(), null)
                seekToStartTag()
            }

        check(parser.name == VECTOR) { "The start tag must be <vector>!" }

        var width = DefaultWidth
        var height = DefaultHeight
        var viewportWidth = DefaultViewportWidth
        var viewportHeight = DefaultViewportHeight
        var autoMirror = DefaultAutoMirror
        val nodes = mutableListOf<VectorNode>()

        var currentGroup: VectorNode.Group? = null

        while (!parser.isAtEnd()) {
            when (parser.eventType) {
                START_TAG -> {
                    when (parser.name) {
                        VECTOR -> {
                            width =
                                parser.getValueAsString(WIDTH)?.processDpDimension() ?: DefaultWidth
                            height = parser.getValueAsString(HEIGHT)?.processDpDimension()
                                ?: DefaultHeight
                            viewportWidth =
                                parser.getValueAsFloat(VIEWPORT_WIDTH) ?: DefaultViewportWidth
                            viewportHeight =
                                parser.getValueAsFloat(VIEWPORT_HEIGHT) ?: DefaultViewportHeight
                            autoMirror = parser.getValueAsString(AUTO_MIRRORED)?.toBoolean()
                                ?: DefaultAutoMirror
                        }

                        PATH -> {
                            val pathData =
                                parser.getAttributeValue(
                                    null,
                                    PATH_DATA,
                                )
                            val fillAlpha = parser.getValueAsFloat(FILL_ALPHA)
                            val fillColor =
                                parser.getValueAsString(FILL_COLOR)?.colorValueToVectorColor()
                            val fillType =
                                when (parser.getAttributeValue(null, FILL_TYPE)) {
                                    // evenOdd and nonZero are the only supported values here, where
                                    // nonZero is the default if no values are defined.
                                    EVEN_ODD -> FillType.EvenOdd

                                    else -> DefaultFillType
                                }
                            val strokeAlpha = parser.getValueAsFloat(STROKE_ALPHA)
                            val strokeCap =
                                when (parser.getAttributeValue(null, STROKE_LINE_CAP)) {
                                    ROUND -> StrokeCap.Round
                                    SQUARE -> StrokeCap.Square
                                    else -> DefaultStrokeCap
                                }
                            val strokeColor =
                                parser.getValueAsString(STROKE_COLOR)?.colorValueToVectorColor()
                            val strokeLineJoin =
                                when (parser.getAttributeValue(null, STROKE_LINE_JOIN)) {
                                    BEVEL -> StrokeJoin.Bevel
                                    ROUND -> StrokeJoin.Round
                                    else -> DefaultStrokeLineJoin
                                }
                            val strokeMiterLimit = parser.getValueAsFloat(STROKE_MITER_LIMIT)
                            val strokeWidth = parser.getValueAsFloat(STROKE_WIDTH)

                            val path =
                                VectorNode.Path(
                                    fillAlpha = fillAlpha ?: DefaultFillAlpha,
                                    fillColor = fillColor,
                                    fillType = fillType,
                                    nodes = PathParser.parsePathString(pathData),
                                    strokeAlpha = strokeAlpha ?: DefaultStrokeAlpha,
                                    strokeCap = strokeCap,
                                    strokeColor = strokeColor,
                                    strokeLineMiter = strokeMiterLimit ?: DefaultStrokeLineMiter,
                                    strokeLineJoin = strokeLineJoin,
                                    strokeWidth = strokeWidth ?: DefaultStrokeWidth,
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
            autoMirror = autoMirror,
            nodes = nodes,
        )
    }
}

/**
 * @return the float value for the attribute [name], or null if it couldn't be found
 */
private fun XmlPullParser.getValueAsFloat(name: String): Float? = getAttributeValue(null, name)?.toFloatOrNull()

/**
 * @return the string value for the attribute [name], or null if it couldn't be found
 */
private fun XmlPullParser.getValueAsString(name: String): String? = getAttributeValue(null, name)?.toString()

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

private fun XmlPullParser.isAtEnd() = eventType == END_DOCUMENT || (depth < 1 && eventType == END_TAG)

private const val ARGB_HEXADECIMAL_COLOR_LENGTH = 9

// XML tag names
private const val VECTOR = "vector"
private const val CLIP_PATH = "clip-path"
private const val GROUP = "group"
private const val PATH = "path"

// XML attribute names
private const val AUTO_MIRRORED = "android:autoMirrored"
private const val FILL_ALPHA = "android:fillAlpha"
private const val FILL_COLOR = "android:fillColor"
private const val FILL_TYPE = "android:fillType"
private const val HEIGHT = "android:height"
private const val PATH_DATA = "android:pathData"
private const val STROKE_ALPHA = "android:strokeAlpha"
private const val STROKE_COLOR = "android:strokeColor"
private const val STROKE_LINE_CAP = "android:strokeLineCap"
private const val STROKE_LINE_JOIN = "android:strokeLineJoin"
private const val STROKE_MITER_LIMIT = "android:strokeMiterLimit"
private const val STROKE_WIDTH = "android:strokeWidth"
private const val VIEWPORT_HEIGHT = "android:viewportHeight"
private const val VIEWPORT_WIDTH = "android:viewportWidth"
private const val WIDTH = "android:width"

// XML attribute values
private const val BEVEL = "bevel"
private const val EVEN_ODD = "evenOdd"
private const val ROUND = "round"
private const val SQUARE = "square"

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

internal fun String.processColor(): String {
    return when {
        this.contains(HEXADECIMAL_SIGN) -> {
            val diff = ARGB_HEXADECIMAL_COLOR_LENGTH - this.length
            if (diff > 0) {
                // Add as many "F" as needed to complete the hexadecimal color length.
                // e.g. "#0A0A0A" -> "#FF0A0A0A"
                this.replace(
                    HEXADECIMAL_SIGN,
                    "$HEXADECIMAL_SIGN${"F".repeat(diff)}",
                ).uppercase()
            } else {
                this.uppercase()
            }
        }
        this.contains(ATTRIBUTE_INDICATOR) -> {
            if (this.contains(ATTRIBUTE_PREFIX)) {
                // Replace the attribute prefix with the attribute indicator.
                // e.g. "?attr/colorPrimary" -> "?colorPrimary"
                this.replace(ATTRIBUTE_PREFIX, ATTRIBUTE_INDICATOR)
            } else {
                this
            }
        }
        else -> {
            DEFAULT_HEXADECIMAL_COLOR
        }
    }
}

private const val ARGB_HEXADECIMAL_COLOR_LENGTH = 9
private const val DEFAULT_HEXADECIMAL_COLOR = "#FF000000"
private const val HEXADECIMAL_SIGN = "#"
private const val ATTRIBUTE_PREFIX = "?attr/"
private const val ATTRIBUTE_INDICATOR = "?"

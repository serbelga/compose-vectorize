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

package dev.sergiobelda.compose.vectorize.compiler.generator.utils

import org.gradle.kotlin.dsl.support.uppercaseFirstChar

/**
 * Converts a snake_case name to a KotlinProperty name.
 *
 * If the first character of [this] is a digit, the resulting name will be prefixed with an `_`
 */
internal fun String.toKotlinPropertyName(): String {
    // Replace any "_" or "-" followed by a letter or digit with the uppercase letter.
    // e.g. "_a" -> "A", "--a" -> "A", "__1" -> "_1", "--1" -> "_1"
    val pattern = "[_-]+[a-z0-9]".toRegex()
    val name = replace(pattern) { match ->
        if (match.value.last().isDigit()) {
            "_${match.value.last()}"
        } else {
            match.value.last().uppercase()
        }
    }

    // If the first character is a digit, prefix the name with an "_".
    return name.let {
        if (it.firstOrNull()?.isDigit() == true) "_$it" else it.uppercaseFirstChar()
    }
}

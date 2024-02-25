package dev.sergiobelda.compose.vectorize.generator.imageparser

internal fun String.processDpDimension(): String =
    this.replace("dp", "")

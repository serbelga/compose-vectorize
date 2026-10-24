plugins {
    alias(deps.plugins.android.kotlinMultiplatformLibrary) apply false
    alias(deps.plugins.android.application) apply false
    alias(deps.plugins.jetbrains.kotlin.composeCompiler) apply false
    alias(deps.plugins.jetbrains.compose) apply false
}

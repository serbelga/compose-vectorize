plugins {
    alias(libs.plugins.android.kotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(deps.plugins.sergiobelda.convention.spotless) apply false
}

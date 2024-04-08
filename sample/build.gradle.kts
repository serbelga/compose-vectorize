plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlin) apply false
}

// Paparazzi workaround for version > 1.3.2
// see: https://github.com/cashapp/paparazzi/releases/tag/1.3.2
subprojects {
    plugins.withId("app.cash.paparazzi") {
        // Defer until afterEvaluate so that testImplementation is created by Android plugin.
        afterEvaluate {
            dependencies.constraints {
                add("testImplementation", "com.google.guava:guava") {
                    attributes {
                        attribute(
                            TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                            objects.named(TargetJvmEnvironment.STANDARD_JVM)
                        )
                    }
                    because("LayoutLib and sdk-common depend on Guava's -jre published variant." +
                            "See https://github.com/cashapp/paparazzi/issues/906.")
                }
            }
        }
    }
}

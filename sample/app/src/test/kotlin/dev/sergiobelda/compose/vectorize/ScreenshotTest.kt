package dev.sergiobelda.compose.vectorize

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.Paparazzi
import dev.sergiobelda.compose.vectorize.sample.common.images.Images
import dev.sergiobelda.compose.vectorize.sample.common.images.illustrations.ComposeMultiplatform
import dev.sergiobelda.compose.vectorize.sample.common.images.illustrations.SwipeOptions
import org.junit.Rule
import org.junit.Test

class ScreenshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun vectorWithStrokeAttribute() {
        paparazzi.snapshot {
            Image(
                imageVector = Images.Illustrations.ComposeMultiplatform,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        }
    }

    @Test
    fun vectorWithThemedAttributes() {
        paparazzi.snapshot {
            Image(
                imageVector = Images.Illustrations.SwipeOptions,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        }
    }
}

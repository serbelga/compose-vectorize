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

package dev.sergiobelda.compose.vectorize

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import dev.sergiobelda.compose.vectorize.sample.common.images.Images
import dev.sergiobelda.compose.vectorize.sample.common.images.illustrations.ComposeMultiplatform
import dev.sergiobelda.compose.vectorize.sample.common.images.illustrations.SwipeOptions
import org.junit.Rule
import org.junit.Test

class ScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            theme = "android:Theme.Material.Light.NoActionBar",
            renderingMode = SessionParams.RenderingMode.SHRINK,
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

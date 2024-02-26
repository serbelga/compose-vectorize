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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sergiobelda.compose.vectorize.sample.common.images.Images
import dev.sergiobelda.compose.vectorize.sample.common.images.icons.outlined.Home
import dev.sergiobelda.compose.vectorize.sample.common.images.icons.rounded.ArrowBack
import dev.sergiobelda.compose.vectorize.sample.common.images.icons.rounded.Home
import dev.sergiobelda.compose.vectorize.sample.common.images.illustrations.ComposeMultiplatform
import dev.sergiobelda.compose.vectorize.sample.common.images.illustrations.SwipeOptions
import dev.sergiobelda.compose.vectorize.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SampleCard(
                title = "Images.Icons.Outlined.Home",
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Images.Icons.Outlined.Home,
                    contentDescription = null,
                )
            }
            SampleCard(
                title = "Images.Icons.Rounded.Home",
            ) {
                Icon(
                    imageVector = Images.Icons.Rounded.Home,
                    contentDescription = null,
                )
            }
            SampleCard(
                title = "Images.Icons.Rounded.ArrowBack",
            ) {
                Icon(
                    imageVector = Images.Icons.Rounded.ArrowBack,
                    contentDescription = null,
                )
            }
            SampleCard(
                title = "Images.Illustrations.ComposeMultiplatform",
            ) {
                Image(
                    imageVector = Images.Illustrations.ComposeMultiplatform,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                )
            }
            SampleCard(
                title = "Images.Illustrations.SwipeOptions",
            ) {
                Image(
                    imageVector = Images.Illustrations.SwipeOptions,
                    contentDescription = null,
                    modifier = Modifier.size(240.dp),
                )
            }
        }
    }
}

@Composable
fun SampleCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(12.dp),
            ) {
                content()
            }
        }
    }
}


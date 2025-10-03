/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.ui.dialogs

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.coroutines.CancellationException

@Composable
fun FullscreenDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    var alpha by remember {
        mutableFloatStateOf(1f)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val windowProvider = LocalView.current.parent as DialogWindowProvider
        LaunchedEffect(Unit) {
            windowProvider.window.setDimAmount(0f)
        }

        PredictiveBackHandler { progress ->
            try {
                progress.collect { state ->
                    alpha = 1 - 1.3f * state.progress
                }
                onDismissRequest.invoke()
            } catch (_: CancellationException) {
                alpha = 1f
            }
        }

        Scaffold(
            modifier = modifier
                .alpha(alpha),
            topBar = topBar
        ) { pV ->
            Box(Modifier.padding(pV)) {
                content.invoke()
            }
        }
    }
}

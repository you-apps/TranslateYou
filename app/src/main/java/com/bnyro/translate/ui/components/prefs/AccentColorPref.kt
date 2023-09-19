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

package com.bnyro.translate.ui.components.prefs

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.bnyro.translate.R
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ui.MainActivity
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.theme.defaultAccentColor
import com.bnyro.translate.util.Preferences
import okhttp3.internal.toHexString

@Composable
fun AccentColorPrefDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val supportsDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    var color by remember {
        mutableStateOf(
            Preferences.getAccentColor() ?: run {
                if (supportsDynamicColors) null else defaultAccentColor
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DialogButton(text = stringResource(R.string.okay)) {
                Preferences.prefs.edit(true) { putString(Preferences.accentColorKey, color) }
                (context as MainActivity).accentColor = color
                onDismissRequest.invoke()
            }
        },
        dismissButton = {
            DialogButton(text = stringResource(R.string.cancel)) {
                onDismissRequest.invoke()
            }
        },
        title = {
            Text(stringResource(R.string.accent_color))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (supportsDynamicColors) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.dynamic_colors)
                        )
                        Switch(
                            checked = color == null,
                            onCheckedChange = { newValue ->
                                color = defaultAccentColor.takeIf { !newValue }
                            }
                        )
                    }
                }

                val isColorPickerEnabled = color != null
                val imageAlpha: Float by animateFloatAsState(
                    targetValue = if (isColorPickerEnabled) 1f else .5f,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing,
                    )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.height(250.dp).alpha(imageAlpha).let {
                        if (isColorPickerEnabled) {
                            it
                        } else {
                            // disable input
                            it.pointerInput(Unit){
                                awaitPointerEventScope {
                                    while (true) {
                                        awaitPointerEvent(pass = PointerEventPass.Initial)
                                            .changes
                                            .forEach(PointerInputChange::consume)
                                    }
                                }
                            }
                        }
                    }
                ) {
                    listOf("R", "G", "B").forEachIndexed { index, c ->
                        val startIndex = index * 2
                        ColorSlider(
                            label = c,
                            value = color?.substring(startIndex, startIndex + 2)?.toInt(16) ?: 0,
                            onChange = { colorInt ->
                                var newHex = colorInt.toHexString()
                                if (newHex.length == 1) newHex = "0$newHex"
                                color = StringBuilder(color).apply {
                                    setCharAt(startIndex, newHex[0])
                                    setCharAt(startIndex + 1, newHex[1])
                                }.toString()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(50.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                )
                        )
                        Text(text = "   =>   ", fontSize = 27.sp)
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color?.hexToColor() ?: Color.Black,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
    )
}

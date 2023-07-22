/*
 * Copyright (c) 2023 Bnyro
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

package com.bnyro.translate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat
import com.bnyro.translate.const.ThemeMode
import com.bnyro.translate.ext.hexToColor

const val defaultAccentColor = "d0bcff"

@Composable
fun TranslateYouTheme(
    themeMode: Int = ThemeMode.AUTO,
    accentColor: Color? = null,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.AUTO -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        else -> true
    }

    val colorScheme = when {
        accentColor == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> {
            val primary = accentColor ?: defaultAccentColor.hexToColor()
            val onPrimary = MaterialTheme.colorScheme.contentColorFor(primary)
            val blendColor =
                if (darkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
            val secondary = Color(ColorUtils.blendARGB(primary.toArgb(), blendColor, 0.3f))
            val onSecondary = MaterialTheme.colorScheme.contentColorFor(secondary)
            if (darkTheme) {
                darkColorScheme(
                    primary,
                    onPrimary,
                    secondary = secondary,
                    onSecondary = onSecondary
                )
            } else {
                lightColorScheme(
                    primary,
                    onPrimary,
                    secondary = secondary,
                    onSecondary = onSecondary
                )
            }
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as Activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.window.navigationBarColor = colorScheme.background.toArgb()
                activity.window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(
                    activity.window,
                    view
                ).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(
                    activity.window,
                    view
                ).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

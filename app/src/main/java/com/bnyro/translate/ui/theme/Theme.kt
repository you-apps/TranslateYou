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

package com.bnyro.translate.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.bnyro.translate.const.ThemeMode
import com.bnyro.translate.ext.hexToColor
import com.google.android.material.color.utilities.CorePalette
import com.google.android.material.color.utilities.Scheme

const val defaultAccentColor = "0088aa"

@Composable
@SuppressLint("RestrictedApi")
fun TranslateYouTheme(
    themeMode: ThemeMode = ThemeMode.AUTO,
    accentColor: Color? = null,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val context = LocalContext.current

    val darkTheme = when (themeMode) {
        ThemeMode.AUTO -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK, ThemeMode.BLACK -> true
    }

    var colorScheme = when {
        accentColor == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> {
            val seed = (accentColor ?: defaultAccentColor.hexToColor()).toArgb()
            val palette = CorePalette.of(seed)
            if (darkTheme)
                Scheme.dark(seed).toColorScheme().fixDarkSurfaceColors(palette)
            else
                Scheme.light(seed).toColorScheme().fixLightSurfaceColors(palette)
        }
    }
    if (themeMode == ThemeMode.BLACK) colorScheme =
        colorScheme.copy(background = Color.Black, surface = Color.Black)

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

@SuppressLint("RestrictedApi")
fun Scheme.toColorScheme() = ColorScheme(
    primary = Color(primary),
    onPrimary = Color(onPrimary),
    primaryContainer = Color(primaryContainer),
    onPrimaryContainer = Color(onPrimaryContainer),
    inversePrimary = Color(inversePrimary),
    secondary = Color(secondary),
    onSecondary = Color(onSecondary),
    secondaryContainer = Color(secondaryContainer),
    onSecondaryContainer = Color(onSecondaryContainer),
    tertiary = Color(tertiary),
    onTertiary = Color(onTertiary),
    tertiaryContainer = Color(tertiaryContainer),
    onTertiaryContainer = Color(onTertiaryContainer),
    background = Color(background),
    onBackground = Color(onBackground),
    surface = Color(surface),
    onSurface = Color(onSurface),
    surfaceVariant = Color(surfaceVariant),
    onSurfaceVariant = Color(onSurfaceVariant),
    surfaceTint = Color(primary),
    inverseSurface = Color(inverseSurface),
    inverseOnSurface = Color(inverseOnSurface),
    error = Color(error),
    onError = Color(onError),
    errorContainer = Color(errorContainer),
    onErrorContainer = Color(onErrorContainer),
    outline = Color(outline),
    outlineVariant = Color(outlineVariant),
    scrim = Color(scrim)
)

// Scheme from material lib don't have this colors.
// Values taken from compose fun dynamicDarkColorScheme31()
@SuppressLint("RestrictedApi")
fun ColorScheme.fixDarkSurfaceColors(palette: CorePalette) = copy(
    surfaceBright = Color(palette.n1.tone(28)),
    surfaceDim = Color(palette.n1.tone(10)),
    surfaceContainer = Color(palette.n1.tone(16)),
    surfaceContainerHigh = Color(palette.n1.tone(21)),
    surfaceContainerHighest = Color(palette.n1.tone(26)),
    surfaceContainerLow = Color(palette.n1.tone(14)),
    surfaceContainerLowest = Color(palette.n1.tone(8))
)

// Scheme from material lib don't have this colors.
// Values taken from compose fun dynamicLightColorScheme31()
@SuppressLint("RestrictedApi")
fun ColorScheme.fixLightSurfaceColors(palette: CorePalette) = copy(
    surfaceBright = Color(palette.n1.tone(99)),
    surfaceDim = Color(palette.n1.tone(88)),
    surfaceContainer = Color(palette.n1.tone(95)),
    surfaceContainerHigh = Color(palette.n1.tone(93)),
    surfaceContainerHighest = Color(palette.n1.tone(91)),
    surfaceContainerLow = Color(palette.n1.tone(97)),
    surfaceContainerLowest = Color(palette.n1.tone(100))
)

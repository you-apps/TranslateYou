package com.bnyro.translate.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.const.ThemeMode
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.util.Preferences

@Composable
fun ThemeModeDialog(
    onDismiss: () -> Unit
) {
    val activity = LocalContext.current as BaseActivity
    ListPreference(
        preferenceKey = Preferences.themeModeKey,
        onDismissRequest = {
            onDismiss.invoke()
        },
        options = listOf(
            ListPreferenceOption(
                name = stringResource(R.string.theme_auto),
                value = ThemeMode.AUTO
            ),
            ListPreferenceOption(
                name = stringResource(R.string.theme_light),
                value = ThemeMode.LIGHT
            ),
            ListPreferenceOption(
                name = stringResource(R.string.theme_dark),
                value = ThemeMode.DARK
            )
        ),
        onOptionSelected = {
            activity.themeMode = it.value
        }
    )
}

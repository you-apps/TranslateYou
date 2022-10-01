package com.bnyro.translate.ui.base

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.util.Preferences

open class BaseActivity : ComponentActivity() {
    var themeMode by mutableStateOf(
        Preferences.getThemeMode()
    )

    fun Content(
        content: @Composable () -> Unit
    ) {
        setContent {
            TranslateYouTheme(themeMode) {
                content()
            }
        }
    }

    override fun onResume() {
        themeMode = Preferences.getThemeMode()
        super.onResume()
    }
}

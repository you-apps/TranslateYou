package com.bnyro.translate.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bnyro.translate.constants.ThemeMode
import com.bnyro.translate.util.Preferences

class ThemeModel : ViewModel() {
    var themeMode by mutableStateOf(
        Preferences.get(
            Preferences.themeModeKey,
            ThemeMode.AUTO.toString()
        ).toInt()
    )
}

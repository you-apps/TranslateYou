package com.bnyro.translate.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.bnyro.translate.models.ThemeModel
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.ui.views.AboutPage

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeModel = ViewModelProvider(this)[ThemeModel::class.java]

        setContent {
            TranslateYouTheme(
                themeModel.themeMode
            ) {
                AboutPage()
            }
        }
    }
}

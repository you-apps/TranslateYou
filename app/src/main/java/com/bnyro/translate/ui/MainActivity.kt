package com.bnyro.translate.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ext.parcelable
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.nav.NavigationHost
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.LocaleHelper
import com.bnyro.translate.util.Preferences
import kotlinx.serialization.encodeToString

class MainActivity : ComponentActivity() {
    private lateinit var mainModel: TranslationModel
    var themeMode by mutableStateOf(
        Preferences.getThemeMode()
    )
    var accentColor by mutableStateOf(Preferences.getAccentColor())

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.updateLanguage(this)

        mainModel = ViewModelProvider(this)[TranslationModel::class.java]

        super.onCreate(savedInstanceState)

        setContent {
            TranslateYouTheme(themeMode, accentColor?.hexToColor()) {
                val navController = rememberNavController()
                NavigationHost(navController, mainModel)
            }
        }

        handleIntentData()
    }

    override fun onStop() {
        Preferences.put(
            Preferences.sourceLanguage,
            JsonHelper.json.encodeToString(mainModel.sourceLanguage)
        )
        Preferences.put(
            Preferences.targetLanguage,
            JsonHelper.json.encodeToString(mainModel.targetLanguage)
        )
        super.onStop()
    }

    @SuppressLint("InlinedApi")
    private fun getIntentText(): String? {
        return intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
            ?: intent.takeIf { Build.VERSION.SDK_INT > Build.VERSION_CODES.M }
                ?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
            ?: intent.getCharSequenceExtra(Intent.ACTION_SEND)?.toString()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        handleIntentData()
    }

    private fun handleIntentData() {
        getIntentText()?.let {
            mainModel.insertedText = it
            mainModel.translateNow()
        }
        if (intent.type?.startsWith("image/") != true) return

        (intent.parcelable<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            mainModel.processImage(this, it)
        }
    }
}

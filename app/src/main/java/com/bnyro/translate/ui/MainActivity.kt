package com.bnyro.translate.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.bnyro.translate.ext.parcelable
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.ui.models.MainModel
import com.bnyro.translate.ui.nav.NavigationHost
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.Preferences
import kotlinx.serialization.encodeToString

class MainActivity : BaseActivity() {
    private lateinit var mainModel: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mainModel = ViewModelProvider(this)[MainModel::class.java]

        super.onCreate(savedInstanceState)

        Content {
            val navController = rememberNavController()
            NavigationHost(navController, mainModel)
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

    private fun getIntentText(): String? {
        intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.let {
            return it.toString()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.let {
                return it.toString()
            }
        }
        intent.getCharSequenceExtra(Intent.ACTION_SEND)?.let {
            return it.toString()
        }
        return null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        handleIntentData()
    }

    private fun handleIntentData() {
        getIntentText()?.let {
            mainModel.insertedText = it
            mainModel.translate()
        }
        if (intent.type?.startsWith("image/") != true) return

        (intent.parcelable<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            mainModel.processImage(this, it)
        }
    }
}

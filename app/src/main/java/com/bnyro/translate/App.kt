package com.bnyro.translate

import android.app.Application
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.SpeechHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Preferences.initialize(
            this
        )

        DatabaseHolder().initDb(
            this
        )

        SpeechHelper.initTTS(this)
    }
}

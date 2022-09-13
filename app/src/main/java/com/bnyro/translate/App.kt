package com.bnyro.translate

import android.app.Application
import com.bnyro.translate.util.RetrofitInstance

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        RetrofitInstance.createApi()
    }
}

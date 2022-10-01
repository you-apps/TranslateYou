package com.bnyro.translate.ui.activities

import android.os.Bundle
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.ui.views.AboutPage

class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Content {
            AboutPage()
        }
    }
}

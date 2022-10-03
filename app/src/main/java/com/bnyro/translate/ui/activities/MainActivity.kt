package com.bnyro.translate.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.ui.models.MainModel
import com.bnyro.translate.ui.views.MainContent
import com.bnyro.translate.ui.views.TopBar
import com.bnyro.translate.util.Preferences
import com.fasterxml.jackson.databind.ObjectMapper

class MainActivity : BaseActivity() {
    lateinit var mainModel: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mainModel = ViewModelProvider(this)[MainModel::class.java]

        super.onCreate(savedInstanceState)
        Content {
            ScreenContent()
        }
        mainModel.fetchLanguages()
    }

    override fun onStop() {
        val mapper = ObjectMapper()
        Preferences.put(
            Preferences.sourceLanguage,
            mapper.writeValueAsString(mainModel.sourceLanguage)
        )
        Preferences.put(
            Preferences.targetLanguage,
            mapper.writeValueAsString(mainModel.targetLanguage)
        )
        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        mainModel.fetchLanguages()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainContent()
        }
    }
}

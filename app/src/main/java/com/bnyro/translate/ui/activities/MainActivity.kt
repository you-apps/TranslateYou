package com.bnyro.translate.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.models.ThemeModel
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.ui.views.MainContent
import com.bnyro.translate.ui.views.TopBar
import com.bnyro.translate.util.Preferences
import com.fasterxml.jackson.databind.ObjectMapper

class MainActivity : ComponentActivity() {
    lateinit var mainModel: MainModel
    lateinit var themeModel: ThemeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mainModel = ViewModelProvider(this)[MainModel::class.java]
        themeModel = ViewModelProvider(this)[ThemeModel::class.java]

        super.onCreate(savedInstanceState)
        setContent {
            TranslateYouTheme(
                themeModel.themeMode
            ) {
                ScreenContent()
            }
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
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent() {
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

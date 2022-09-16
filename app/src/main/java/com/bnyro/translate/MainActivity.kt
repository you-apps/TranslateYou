package com.bnyro.translate

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.models.NavigationModel
import com.bnyro.translate.models.ThemeModel
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.ui.views.AboutPage
import com.bnyro.translate.ui.views.MainContent
import com.bnyro.translate.ui.views.OptionsDialog
import com.bnyro.translate.ui.views.TopBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainModel = ViewModelProvider(this)[MainModel::class.java]
        val themeModel = ViewModelProvider(this)[ThemeModel::class.java]
        setContent {
            TranslateYouTheme(
                themeModel.themeMode
            ) {
                ScreenContent()
            }
        }
        mainModel.fetchLanguages()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent() {
    val navigationModel: NavigationModel = viewModel()

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

    if (navigationModel.showOptions) OptionsDialog()
    if (navigationModel.showAbout) AboutPage()
}

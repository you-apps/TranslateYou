package com.bnyro.translate.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bnyro.translate.ui.models.MainModel
import com.bnyro.translate.ui.screens.HistoryScreen
import com.bnyro.translate.ui.screens.SettingsScreen
import com.bnyro.translate.ui.screens.TranslationPage
import com.bnyro.translate.ui.views.AboutPage

@Composable
fun NavigationHost(
    navController: NavHostController,
    translationModel: MainModel
) {
    NavHost(navController = navController, "translate") {
        composable("translate") {
            TranslationPage(navController, translationModel)
        }
        composable("history") {
            HistoryScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("about") {
            AboutPage()
        }
    }
}

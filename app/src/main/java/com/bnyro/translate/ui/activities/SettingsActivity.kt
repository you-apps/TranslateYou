package com.bnyro.translate.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.ThemeModeDialog
import com.bnyro.translate.ui.components.prefs.SettingsCategory
import com.bnyro.translate.ui.components.prefs.SliderPreference
import com.bnyro.translate.ui.components.prefs.SwitchPreference
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.ui.views.EnginePref
import com.bnyro.translate.util.Preferences

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Content {
            TranslateYouTheme(
                themeMode
            ) {
                SettingsPage()
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SettingsPage() {
    val context = LocalContext.current

    var showThemeOptions by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.options)
                    )
                },
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack
                    ) {
                        (context as Activity).finish()
                    }
                },
                actions = {
                    StyledIconButton(
                        imageVector = Icons.Default.DarkMode
                    ) {
                        showThemeOptions = true
                    }
                }
            )
        }
    ) { pV ->
        Column(
            modifier = Modifier
                .padding(pV)
                .fillMaxSize()
                .padding(15.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            EnginePref()

            SettingsCategory(
                title = stringResource(R.string.history)
            )

            SwitchPreference(
                preferenceKey = Preferences.historyEnabledKey,
                defaultValue = true,
                preferenceTitle = stringResource(R.string.history_enabled),
                preferenceSummary = stringResource(R.string.history_summary)
            )

            SliderPreference(
                preferenceKey = Preferences.fetchDelay,
                preferenceTitle = stringResource(R.string.fetch_delay),
                preferenceSummary = stringResource(R.string.fetch_delay_summary),
                defaultValue = 500f,
                minValue = 100f,
                maxValue = 1000f,
                stepSize = 100f
            )
        }
    }

    if (showThemeOptions) {
        ThemeModeDialog {
            showThemeOptions = false
        }
    }
}

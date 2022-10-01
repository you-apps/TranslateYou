package com.bnyro.translate.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.models.ThemeModel
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.ThemeModeDialog
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.util.Preferences

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeModel = ViewModelProvider(this)[ThemeModel::class.java]

        setContent {
            TranslateYouTheme(
                themeModel.themeMode
            ) {
                SettingsPage()
            }
        }
    }
}

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
                title = {},
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
        },
        content = {
            var instanceUrl by remember {
                mutableStateOf(
                    Preferences.get(
                        Preferences.instanceUrlKey,
                        Preferences.defaultInstanceUrl
                    )
                )
            }

            var apiKey by remember {
                mutableStateOf(
                    Preferences.get(
                        Preferences.apiKey,
                        ""
                    )
                )
            }

            Column(
                modifier = Modifier.padding(it)
            ) {
                OutlinedTextField(
                    value = instanceUrl,
                    onValueChange = { instanceUrl = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.instance
                            )
                        )
                    }
                )

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.api_key
                            )
                        )
                    },
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 0.dp)
                )
            }
        }
    )

    val themeModel: ThemeModel = viewModel()

    if (showThemeOptions) {
        ThemeModeDialog(
            onDismiss = {
                showThemeOptions = false
            },
            onThemeModeChanged = {
                themeModel.themeMode = it
            }
        )
    }
}

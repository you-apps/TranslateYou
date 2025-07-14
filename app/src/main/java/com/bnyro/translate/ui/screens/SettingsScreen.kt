/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.ui.MainActivity
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.ThemeModeDialog
import com.bnyro.translate.ui.components.prefs.AccentColorPrefDialog
import com.bnyro.translate.ui.components.prefs.ListPreference
import com.bnyro.translate.ui.components.prefs.PreferenceItem
import com.bnyro.translate.ui.components.prefs.SettingsCategory
import com.bnyro.translate.ui.components.prefs.SliderPreference
import com.bnyro.translate.ui.components.prefs.SwitchPreference
import com.bnyro.translate.ui.dialogs.EngineSelectionDialog
import com.bnyro.translate.ui.views.EnginePref
import com.bnyro.translate.ui.views.TessSettings
import com.bnyro.translate.util.LocaleHelper
import com.bnyro.translate.util.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var showThemeOptions by remember {
        mutableStateOf(false)
    }

    var showAccentColorDialog by remember {
        mutableStateOf(false)
    }

    var enableSimultaneousTranslation by remember {
        mutableStateOf(
            Preferences.get(Preferences.simultaneousTranslationKey, false)
        )
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    var showEngineSelectDialog by remember {
        mutableStateOf(false)
    }

    var showTessSettings by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.options)
                    )
                },
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack
                    ) {
                        navController.popBackStack()
                    }
                },
                actions = {
                    StyledIconButton(
                        imageVector = Icons.Default.Palette
                    ) {
                        showAccentColorDialog = true
                    }
                    StyledIconButton(
                        imageVector = Icons.Default.DarkMode
                    ) {
                        showThemeOptions = true
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { pV ->
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(pV)
                .fillMaxSize()
                .padding(15.dp, 0.dp)
        ) {
            item {
                SettingsCategory(
                    title = stringResource(R.string.translation_engine)
                )

                EnginePref()
            }

            item {
                SettingsCategory(
                    title = stringResource(R.string.general)
                )

                val appLanguages = LocaleHelper.getLanguages(context)

                ListPreference(
                    title = stringResource(R.string.app_language),
                    preferenceKey = Preferences.appLanguageKey,
                    defaultValue = "",
                    entries = appLanguages.map { it.name },
                    values = appLanguages.map { it.code }
                ) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        (context as MainActivity).recreate()
                    }, 100)
                }
            }

            item {
                PreferenceItem(
                    modifier = Modifier.padding(top = 10.dp),
                    title = stringResource(R.string.image_translation),
                    summary = stringResource(R.string.image_translation_summary)
                ) {
                    showTessSettings = true
                }
            }

            item {
                SettingsCategory(
                    title = stringResource(R.string.history)
                )

                SwitchPreference(
                    preferenceKey = Preferences.historyEnabledKey,
                    defaultValue = true,
                    preferenceTitle = stringResource(R.string.history_enabled),
                    preferenceSummary = stringResource(R.string.history_summary)
                )

                SwitchPreference(
                    preferenceKey = Preferences.compactHistory,
                    defaultValue = true,
                    preferenceTitle = stringResource(R.string.compact_history),
                    preferenceSummary = stringResource(R.string.compact_history_summary)
                )

                SwitchPreference(
                    preferenceKey = Preferences.skipSimilarHistoryKey,
                    defaultValue = true,
                    preferenceTitle = stringResource(R.string.skip_similar_entries),
                    preferenceSummary = stringResource(R.string.skip_similar_entries_desc)
                )
            }

            item {
                SettingsCategory(
                    title = stringResource(R.string.translation)
                )

                var translateAutomatically by remember {
                    mutableStateOf(Preferences.get(Preferences.translateAutomatically, true))
                }

                SwitchPreference(
                    preferenceKey = Preferences.translateAutomatically,
                    defaultValue = true,
                    preferenceTitle = stringResource(R.string.translate_automatically),
                    preferenceSummary = stringResource(R.string.translate_automatically_summary)
                ) {
                    translateAutomatically = it
                }

                AnimatedVisibility(visible = translateAutomatically) {
                    SliderPreference(
                        preferenceKey = Preferences.fetchDelay,
                        preferenceTitle = stringResource(R.string.fetch_delay),
                        preferenceSummary = stringResource(R.string.fetch_delay_summary),
                        defaultValue = 800f,
                        minValue = 100f,
                        maxValue = 2000f,
                        stepSize = 100f
                    )
                }

                SwitchPreference(
                    preferenceKey = Preferences.showAdditionalInfo,
                    defaultValue = true,
                    preferenceTitle = stringResource(R.string.additional_info),
                    preferenceSummary = stringResource(R.string.additional_info_summary)
                )
            }

            item {
                SettingsCategory(
                    title = stringResource(R.string.simultaneous_translation)
                )

                SwitchPreference(
                    preferenceKey = Preferences.simultaneousTranslationKey,
                    defaultValue = false,
                    preferenceTitle = stringResource(R.string.simultaneous_translation),
                    preferenceSummary = stringResource(
                        R.string.simultaneous_translation_summary
                    )
                ) {
                    enableSimultaneousTranslation = it
                }

                AnimatedVisibility(visible = enableSimultaneousTranslation) {
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    PreferenceItem(
                        title = stringResource(R.string.enabled_engines),
                        summary = stringResource(R.string.enabled_engines_summary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        showEngineSelectDialog = true
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                val charCounterLimits = listOf(
                    stringResource(R.string.none),
                    "50",
                    "100",
                    "150",
                    "200",
                    "300",
                    "400",
                    "500",
                    "1000",
                    "2000",
                    "3000",
                    "5000"
                )
                ListPreference(
                    title = stringResource(R.string.character_warning_limit),
                    summary = stringResource(R.string.character_warning_limit_summary),
                    preferenceKey = Preferences.charCounterLimitKey,
                    defaultValue = charCounterLimits.first(),
                    entries = charCounterLimits,
                    values = charCounterLimits.map {
                        if (it.all { char -> char.isDigit() }) it else ""
                    }
                )

                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                )
            }
        }
    }

    if (showEngineSelectDialog) {
        EngineSelectionDialog {
            showEngineSelectDialog = false
        }
    }

    if (showThemeOptions) {
        ThemeModeDialog {
            showThemeOptions = false
        }
    }

    if (showTessSettings) {
        TessSettings {
            showTessSettings = false
        }
    }

    if (showAccentColorDialog) {
        AccentColorPrefDialog {
            showAccentColorDialog = false
        }
    }
}

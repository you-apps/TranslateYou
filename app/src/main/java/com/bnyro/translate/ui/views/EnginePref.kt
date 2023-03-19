package com.bnyro.translate.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.api.st.STEngine
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.ui.components.BlockRadioButton
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.components.SelectableItem
import com.bnyro.translate.ui.components.prefs.EditTextPreference
import com.bnyro.translate.ui.components.prefs.PreferenceItem
import com.bnyro.translate.util.Preferences
import java.util.*

@Composable
fun EnginePref() {
    val engines = TranslationEngines.engines

    var selected by remember {
        mutableStateOf(
            Preferences.get(
                Preferences.apiTypeKey,
                0
            )
        )
    }

    var instanceUrl by remember {
        mutableStateOf(
            engines[selected].getUrl()
        )
    }

    var apiKey by remember {
        mutableStateOf(
            engines[selected].getApiKey()
        )
    }

    BlockRadioButton(
        items = engines.map { it.name },
        selected = selected,
        onSelect = {
            selected = it
            Preferences.put(
                Preferences.apiTypeKey,
                selected
            )
            instanceUrl = engines[selected].getUrl()
            apiKey = engines[selected].getApiKey()
            TranslationEngines.updateAll()
        }
    ) {
        engines[selected].apply {
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )

            if (this.urlModifiable) {
                EditTextPreference(
                    preferenceKey = this.urlPrefKey,
                    value = instanceUrl,
                    onValueChange = {
                        instanceUrl = it
                    },
                    labelText = stringResource(R.string.instance)
                )
            }

            if (this.apiKeyState != ApiKeyState.DISABLED) {
                EditTextPreference(
                    preferenceKey = this.apiPrefKey,
                    value = apiKey,
                    labelText = stringResource(
                        id = R.string.api_key
                    ) + when (apiKeyState) {
                        ApiKeyState.REQUIRED -> " (${stringResource(R.string.required)})"
                        ApiKeyState.OPTIONAL -> " (${stringResource(R.string.optional)})"
                        else -> ""
                    }
                ) {
                    apiKey = it
                }
            }

            (this as? STEngine)?.let {
                val avEngines = listOf("all", "google", "libre", "reverso", "iciba")

                var showEngineSelDialog by remember {
                    mutableStateOf(false)
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                PreferenceItem(
                    title = stringResource(R.string.selected_engine),
                    summary = stringResource(R.string.st_selected_engine)
                ) {
                    showEngineSelDialog = true
                }

                if (showEngineSelDialog) {
                    AlertDialog(
                        onDismissRequest = { showEngineSelDialog = false },
                        confirmButton = {
                            DialogButton(
                                stringResource(R.string.cancel)
                            ) {
                                showEngineSelDialog = false
                            }
                        },
                        text = {
                            LazyColumn {
                                items(avEngines) {
                                    SelectableItem(
                                        text = it.replaceFirstChar {
                                            if (it.isLowerCase()) {
                                                it.titlecase(
                                                    Locale.getDefault()
                                                )
                                            } else {
                                                it.toString()
                                            }
                                        }
                                    ) {
                                        Preferences.put(selEnginePrefKey, it)
                                        showEngineSelDialog = false
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

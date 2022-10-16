package com.bnyro.translate.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.ui.components.BlockRadioButton
import com.bnyro.translate.ui.components.prefs.EditTextPreference
import com.bnyro.translate.util.Preferences

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
            TranslationEngines.update()
        },
        content = {
            engines[selected].apply {
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
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

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

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
            }
        }
    )
}

package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.constants.ApiKeyState
import com.bnyro.translate.constants.TranslationEngines
import com.bnyro.translate.obj.TranslationEngine
import com.bnyro.translate.ui.components.prefs.EditTextPreference
import com.bnyro.translate.util.Preferences

@Composable
fun BlockRadioButton(
    selected: Int = 0,
    onSelect: (Int) -> Unit,
    engines: List<TranslationEngine>
) {
    var instanceUrl by remember {
        mutableStateOf(
            Preferences.getUrlByEngine(engines[selected])
        )
    }

    var apiKey by remember {
        mutableStateOf(
            Preferences.getApiKeyByEngine(engines[selected])
        )
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            engines.forEachIndexed { index, engine ->
                BlockButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp, 0.dp),
                    text = engine.name,
                    selected = selected == index
                ) {
                    onSelect(index)
                    instanceUrl = Preferences.getUrlByEngine(engines[selected])
                    apiKey = Preferences.getApiKeyByEngine(engines[selected])
                }
            }
        }
        engines[selected].apply {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            if (this.urlModifiable) {
                EditTextPreference(
                    preferenceKey = this.name + Preferences.instanceUrlKey,
                    value = instanceUrl,
                    onValueChange = {
                        instanceUrl = it
                        TranslationEngines.update()
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
                    preferenceKey = this.name + Preferences.apiKey,
                    value = apiKey,
                    labelText = stringResource(
                        id = R.string.api_key
                    )
                ) {
                    apiKey = it
                }
            }
        }
    }
}

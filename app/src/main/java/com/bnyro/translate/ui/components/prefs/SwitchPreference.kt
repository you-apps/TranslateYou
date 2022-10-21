package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.translate.util.Preferences

@Composable
fun SwitchPreference(
    preferenceKey: String,
    defaultValue: Boolean,
    preferenceTitle: String,
    preferenceSummary: String,
    onValueChange: (Boolean) -> Unit = {}
) {
    var checked by remember {
        mutableStateOf(
            Preferences.get(
                preferenceKey,
                defaultValue
            )
        )
    }

    fun onCheckedChange(newValue: Boolean) {
        checked = newValue
        Preferences.put(
            preferenceKey,
            checked
        )
        onValueChange.invoke(newValue)
    }

    val indicationSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp)
            .clickable(
                indicationSource,
                null
            ) {
                onCheckedChange(!checked)
            }
    ) {
        PreferenceItem(
            title = preferenceTitle,
            summary = preferenceSummary,
            modifier = Modifier
                .weight(1.0f)
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(!checked)
            }
        )
    }
}

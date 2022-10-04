package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.translate.util.Preferences

@Composable
fun SwitchPreference(
    preferenceKey: String,
    defaultValue: Boolean,
    preferenceTitle: String
) {
    var checked by remember {
        mutableStateOf(
            Preferences.get(
                preferenceKey,
                defaultValue
            )
        )
    }

    val indicationSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(5.dp, 5.dp)
            .clickable(
                indicationSource,
                null
            ) {
                checked = !checked
                Preferences.put(
                    preferenceKey,
                    checked
                )
            }
    ) {
        Text(
            preferenceTitle,
            modifier = Modifier
                .weight(1.0f)
                .align(Alignment.CenterVertically),
            fontSize = 18.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                Preferences.put(
                    preferenceKey,
                    it
                )
            }
        )
    }
}

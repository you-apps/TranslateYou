package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.translate.ui.components.SliderWithLabel
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.getStepCount

@Composable
fun SliderPreference(
    preferenceKey: String,
    preferenceTitle: String,
    defaultValue: Float,
    minValue: Float,
    maxValue: Float,
    stepSize: Float
) {
    var value by remember {
        mutableStateOf(
            Preferences.get(
                preferenceKey,
                defaultValue
            )
        )
    }

    Column(
        modifier = Modifier
            .padding(
                top = 20.dp
            )
    ) {
        PreferenceItem(
            title = preferenceTitle,
            summary = preferenceTitle,
            modifier = Modifier.weight(1.0f)
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        SliderWithLabel(
            value = value,
            onValueChange = {
                value = it
                Preferences.put(
                    preferenceKey,
                    it
                )
            },
            valueRange = minValue..maxValue,
            steps = getStepCount(
                minValue,
                maxValue,
                stepSize
            )
        )
    }
}

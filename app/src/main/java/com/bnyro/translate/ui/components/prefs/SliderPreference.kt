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

@Composable
fun SliderPreference(
    preferenceKey: String,
    preferenceTitle: String,
    preferenceSummary: String,
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
                top = 5.dp
            )
    ) {
        PreferenceItem(
            title = preferenceTitle,
            summary = preferenceSummary
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

private fun getStepCount(minValue: Float, maxValue: Float, stepSize: Float): Int {
    return ((maxValue - minValue) / stepSize).toInt() - 1
}

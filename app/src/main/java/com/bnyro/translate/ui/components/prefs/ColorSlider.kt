package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColorSlider(
    label: String,
    value: Int,
    onChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Slider(
            modifier = Modifier.padding(horizontal = 10.dp).weight(1f),
            value = value.toFloat(),
            valueRange = 0f..255f,
            steps = 256,
            onValueChange = {
                onChange(it.toInt())
            }
        )
        Text(value.toString())
    }
}

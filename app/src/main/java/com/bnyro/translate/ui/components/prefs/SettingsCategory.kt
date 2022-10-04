package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsCategory(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 40.dp,
                bottom = 5.dp,
                start = 5.dp,
                end = 5.dp
            )
    ) {
        Text(
            text = title,
            fontSize = 14.sp
        )
    }
}

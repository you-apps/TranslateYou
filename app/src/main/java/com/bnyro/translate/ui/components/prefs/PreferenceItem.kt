package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceItem(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row {
        Column {
            Text(title)
            Spacer(Modifier.height(2.dp))
            Text(summary, fontSize = 12.sp)
        }
    }
}

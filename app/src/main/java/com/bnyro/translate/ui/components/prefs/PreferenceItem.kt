package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceItem(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick.invoke()
            }
    ) {
        Column {
            Text(title)
            Spacer(Modifier.height(4.dp))
            Text(
                text = summary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

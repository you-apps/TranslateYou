package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BlockRadioButton(
    selected: Int = 0,
    onSelect: (Int) -> Unit,
    items: List<String>,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                BlockButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp, 0.dp),
                    text = item,
                    selected = selected == index
                ) {
                    onSelect.invoke(index)
                }
            }
        }
        content.invoke()
    }
}

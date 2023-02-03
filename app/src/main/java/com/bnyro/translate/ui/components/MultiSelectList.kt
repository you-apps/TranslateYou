package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultiSelectList(
    titles: List<String>,
    selectedItems: BooleanArray,
    onChange: (BooleanArray) -> Unit
) {
    Column {
        titles.forEachIndexed { index, title ->
            var selected by remember {
                mutableStateOf(selectedItems[index])
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                )
                Text(title)
                Spacer(
                    modifier = Modifier
                        .weight(1.0f)
                )
                Checkbox(
                    checked = selected,
                    onCheckedChange = {
                        selectedItems[index] = it
                        selected = it
                        onChange.invoke(selectedItems)
                    }
                )
            }
        }
    }
}

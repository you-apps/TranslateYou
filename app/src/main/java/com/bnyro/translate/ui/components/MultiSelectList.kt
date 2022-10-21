package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MultiSelectList(
    titles: List<String>,
    selectedItems: BooleanArray,
    onChange: (BooleanArray) -> Unit
) {
    Column {
        titles.forEachIndexed { index, title ->
            Row {
                Checkbox(
                    checked = selectedItems[index],
                    onCheckedChange = {
                        selectedItems[index] = it
                        onChange.invoke(selectedItems)
                    }
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(title)
            }
        }
    }
}

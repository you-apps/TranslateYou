package com.bnyro.translate.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ui.components.StyledIconButton

@Composable
fun HistoryRow(
    historyItem: HistoryItem,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // TODO() Start Main Activity with data
            }
            .padding(
                start = 15.dp,
                end = 5.dp,
                top = 8.dp,
                bottom = 8.dp
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f)

        ) {
            Text(
                historyItem.insertedText,
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )

            Text(
                historyItem.translatedText,
                fontSize = 14.sp
            )
        }
        StyledIconButton(
            imageVector = Icons.Default.Delete
        ) {
            onDelete.invoke()
        }
    }
}

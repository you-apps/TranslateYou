package com.bnyro.translate.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectableItem(
    text: String,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(30.dp)
            )
            .clickable {
                onClick.invoke()
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
    }
}

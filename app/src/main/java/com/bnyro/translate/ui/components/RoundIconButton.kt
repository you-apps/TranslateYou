package com.bnyro.translate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RoundIconButton(
    backgroundColor: Color,
    contentDescription: String,
    iconResourceId: Int,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .size(70.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            ),
        onClick = {
            onClick.invoke()
        }
    ) {
        Icon(
            painter = painterResource(iconResourceId),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

package com.bnyro.translate.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun StyledIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = {
            onClick.invoke()
        }
    ) {
        Icon(
            modifier = modifier,
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

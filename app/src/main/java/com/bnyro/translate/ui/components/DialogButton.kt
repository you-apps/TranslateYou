package com.bnyro.translate.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DialogButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick
    ) {
        Text(text)
    }
}

package com.bnyro.translate.util

import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimTranslationChips() {
    ElevatedFilterChip(
        selected = false,
        onClick = {
        },
        label = {
            Text("")
        }
    )
}

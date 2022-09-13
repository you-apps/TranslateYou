package com.bnyro.translate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun LanguageSelector() {
    var expanded by remember {
        mutableStateOf(false)
    }
    val suggestions = listOf("Item1", "Item2", "Item3")

    ElevatedButton(onClick = { expanded = !expanded }) {
        Text("DropDown")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        suggestions.forEach { label ->
            DropdownMenuItem(
                onClick = {
                    expanded = false
                },
                text = {
                    Text(text = label)
                }
            )
        }
    }
}

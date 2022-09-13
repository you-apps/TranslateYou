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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.obj.Language

@Composable
fun LanguageSelector(
    languages: List<Language>,
    onClick: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var text by remember {
        mutableStateOf("English")
    }

    ElevatedButton(onClick = { expanded = !expanded }) {
        Text(text)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        languages.forEach {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    text = it.name!!
                    onClick.invoke(it.code!!)
                },
                text = {
                    Text(text = it.name!!)
                }
            )
        }
    }
}

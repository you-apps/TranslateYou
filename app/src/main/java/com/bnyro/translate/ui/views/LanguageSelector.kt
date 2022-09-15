package com.bnyro.translate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.obj.Language

@Composable
fun LanguageSelector(
    languages: List<Language>,
    selectedLanguage: Language,
    onClick: (Language) -> Unit
) {
    val viewModel: MainModel = viewModel()

    var expanded by remember {
        mutableStateOf(false)
    }

    ElevatedButton(
        onClick = { expanded = !expanded },
        modifier = Modifier
            .padding(5.dp)
    ) {
        Text(
            text = selectedLanguage.name!!
        )
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
                    viewModel.translate()
                    onClick.invoke(it)
                },
                text = {
                    Text(text = it.name!!)
                }
            )
        }
    }
}

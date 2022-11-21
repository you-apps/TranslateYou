package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.obj.Language
import com.bnyro.translate.ui.models.MainModel

@Composable
fun LanguageSelector(
    availableLanguages: List<Language>,
    selectedLanguage: Language,
    autoLanguageEnabled: Boolean = false,
    onClick: (Language) -> Unit
) {
    val viewModel: MainModel = viewModel()

    var showDialog by remember {
        mutableStateOf(false)
    }

    ElevatedButton(
        onClick = { showDialog = !showDialog },
        modifier = Modifier
            .padding(5.dp)
    ) {
        Text(
            text = selectedLanguage.name
        )
    }

    val languages = availableLanguages.toMutableList()

    // remove auto language
    if (autoLanguageEnabled && languages.isNotEmpty()) {
        languages.add(
            0,
            Language("", stringResource(R.string.auto))
        )
    }

    var favoriteLanguages by remember {
        mutableStateOf(listOf<Language>())
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            text = {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(favoriteLanguages) {
                        LanguageItem(
                            language = it,
                            isPinned = favoriteLanguages.contains(it),
                            onClick = {
                                showDialog = false
                                viewModel.enqueueTranslation()
                                onClick.invoke(it)
                            },
                            onPinnedChange = {
                                favoriteLanguages = favoriteLanguages.filter { language ->
                                    it != language
                                }
                            }
                        )
                    }

                    item {
                        if (favoriteLanguages.isNotEmpty()) {
                            Divider(
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .size(70.dp, 1.dp)
                            )
                        }
                    }

                    items(languages) {
                        LanguageItem(
                            language = it,
                            isPinned = favoriteLanguages.contains(it),
                            onClick = {
                                showDialog = false
                                viewModel.enqueueTranslation()
                                onClick.invoke(it)
                            },
                            onPinnedChange = {
                                favoriteLanguages = if (favoriteLanguages.contains(it)) {
                                    favoriteLanguages - it
                                } else {
                                    favoriteLanguages + it
                                }
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(
                        stringResource(
                            id = R.string.cancel
                        )
                    )
                }
            }
        )
    }
}

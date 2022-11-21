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
import com.bnyro.translate.DatabaseHolder
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.query
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            text = {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(viewModel.bookmarkedLanguages) {
                        LanguageItem(
                            language = it,
                            isPinned = viewModel.bookmarkedLanguages.contains(it),
                            onClick = {
                                showDialog = false
                                viewModel.enqueueTranslation()
                                onClick.invoke(it)
                            },
                            onPinnedChange = {
                                viewModel.bookmarkedLanguages = viewModel.bookmarkedLanguages.filter { language ->
                                    it != language
                                }
                                query {
                                    DatabaseHolder.Db.languageBookmarksDao().delete(it)
                                }
                            }
                        )
                    }

                    item {
                        if (viewModel.bookmarkedLanguages.isNotEmpty()) {
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
                            isPinned = viewModel.bookmarkedLanguages.contains(it),
                            onClick = {
                                showDialog = false
                                viewModel.enqueueTranslation()
                                onClick.invoke(it)
                            },
                            onPinnedChange = {
                                viewModel.bookmarkedLanguages = if (viewModel.bookmarkedLanguages.contains(it)) {
                                    query {
                                        DatabaseHolder.Db.languageBookmarksDao().delete(it)
                                    }
                                    viewModel.bookmarkedLanguages - it
                                } else {
                                    query {
                                        DatabaseHolder.Db.languageBookmarksDao().insertAll(it)
                                    }
                                    viewModel.bookmarkedLanguages + it
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

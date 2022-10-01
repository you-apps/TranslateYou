package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.obj.Language
import com.bnyro.translate.ui.models.MainModel

@Composable
fun LanguageSelector(
    languages: List<Language>,
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
            text = selectedLanguage.name!!
        )
    }

    val autoText = stringResource(
        id = R.string.auto
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            text = {
                LazyColumn {
                    if (autoLanguageEnabled) {
                        item() {
                            val it = Language(
                                code = "auto",
                                name = autoText
                            )

                            SelectableItem(
                                text = it.name!!,
                                onClick = {
                                    showDialog = false
                                    viewModel.enqueueTranslation()
                                    onClick.invoke(it)
                                }
                            )
                        }
                    }
                    items(languages) {
                        SelectableItem(
                            text = it.name!!,
                            onClick = {
                                showDialog = false
                                viewModel.enqueueTranslation()
                                onClick.invoke(it)
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

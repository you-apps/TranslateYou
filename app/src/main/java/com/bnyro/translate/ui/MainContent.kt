package com.bnyro.translate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.models.OptionsModel

@Composable
fun MainContent(
    mainModel: MainModel = viewModel(),
    optionsModel: OptionsModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .weight(1.0f)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                StyledTextField(
                    text = mainModel.insertedText,
                    onValueChange = {
                        mainModel.insertedText = it
                        mainModel.translate()
                    },
                    placeholder = stringResource(R.string.enter_text)
                )

                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .size(70.dp, 1.dp)
                )

                StyledTextField(
                    text = mainModel.translatedText,
                    onValueChange = {},
                    readOnly = true
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageSelector(
                mainModel.availableLanguages,
                mainModel.sourceLanguage
            ) {
                mainModel.sourceLanguage = it
            }

            IconButton(
                onClick = {
                    val temp = mainModel.sourceLanguage
                    mainModel.sourceLanguage = mainModel.targetLanguage
                    mainModel.targetLanguage = temp
                }
            ) {
                Icon(
                    Icons.Default.CompareArrows,
                    null
                )
            }

            LanguageSelector(
                mainModel.availableLanguages,
                mainModel.targetLanguage
            ) {
                mainModel.targetLanguage = it
            }
        }
    }

    if (optionsModel.openDialog) {
        OptionsDialog(
            onDismissRequest = {
                optionsModel.openDialog = false
            }
        )
    }
}

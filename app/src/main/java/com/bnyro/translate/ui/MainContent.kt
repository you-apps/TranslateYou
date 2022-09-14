package com.bnyro.translate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.outlined.CompareArrows
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
import com.bnyro.translate.R
import com.bnyro.translate.models.MainModel

@Composable
fun MainContent(
    viewModel: MainModel = androidx.lifecycle.viewmodel.compose.viewModel()
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
                    text = viewModel.insertedText,
                    onValueChange = {
                        viewModel.insertedText = it
                        viewModel.translate()
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
                    text = viewModel.translatedText,
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
                viewModel.availableLanguages,
                viewModel.sourceLanguage
            ) {
                viewModel.sourceLanguage = it
            }

            IconButton(
                onClick = {
                    val temp = viewModel.sourceLanguage
                    viewModel.sourceLanguage = viewModel.targetLanguage
                    viewModel.targetLanguage = temp
                }
            ) {
                Icon(
                    Icons.Default.CompareArrows,
                    null
                )
            }

            LanguageSelector(
                viewModel.availableLanguages,
                viewModel.targetLanguage
            ) {
                viewModel.targetLanguage = it
            }
        }
    }
}

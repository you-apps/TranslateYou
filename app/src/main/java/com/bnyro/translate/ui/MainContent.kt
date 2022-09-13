package com.bnyro.translate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.translate.models.MainModel

@Composable
fun MainContent(
    viewModel: MainModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
            ) {
                StyledTextField(
                    text = viewModel.insertedText,
                    onValueChange = {
                        viewModel.insertedText = it
                        viewModel.translate()
                    },
                    modifier = Modifier
                        .padding(0.dp, 50.dp, 0.dp, 0.dp)
                )
                StyledTextField(
                    text = viewModel.translation,
                    onValueChange = {},
                    readOnly = true
                )
            }
            Row {
                LanguageSelector(
                    viewModel.availableLanguages
                ) { source ->
                    viewModel.sourceLanguage = source
                }
                LanguageSelector(
                    viewModel.availableLanguages
                ) { target ->
                    viewModel.targetLanguage = target
                }
            }
        }
    }
}

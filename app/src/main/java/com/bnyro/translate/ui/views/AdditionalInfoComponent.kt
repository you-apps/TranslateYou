package com.bnyro.translate.ui.views

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.ui.components.AdditionalInfo
import com.bnyro.translate.ui.models.MainModel

@Composable
fun AdditionalInfoComponent(
    translation: Translation
) {
    val viewModel: MainModel = viewModel()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .heightIn(0.dp, 200.dp)
        ) {
            translation.detectedLanguage?.let { language ->
                item(language) {
                    AdditionalInfo(
                        title = stringResource(R.string.detected_language),
                        text = try {
                            viewModel.availableLanguages.first {
                                it.code == language
                            }.name
                        } catch (e: Exception) {
                            language
                        }
                    )
                }
            }
            translation.transliterations?.let { transliteration ->
                items(transliteration) {
                    AdditionalInfo(
                        title = stringResource(R.string.transliteration),
                        text = it
                    )
                }
            }
            translation.definitions?.let { definition ->
                items(definition) {
                    AdditionalInfo(
                        title = stringResource(R.string.definition),
                        text = listOfNotNull(it.type, it.definition, it.example, it.synonym).joinToString(
                            ", "
                        )
                    )
                }
            }
            translation.similar?.let { synonym ->
                items(synonym) {
                    AdditionalInfo(
                        title = stringResource(R.string.similar),
                        text = it
                    )
                }
            }
            translation.examples?.let { example ->
                items(example) {
                    AdditionalInfo(
                        title = stringResource(R.string.example),
                        text = it
                    )
                }
            }
        }
    }
}

package com.bnyro.translate.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.ui.components.LanguageSelector
import com.bnyro.translate.ui.components.StyledTextField
import com.bnyro.translate.util.ClipboardHelper

@Composable
fun MainContent(
    viewModel: MainModel = viewModel()
) {
    val focusRequester = remember {
        FocusRequester()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .weight(1.0f)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize()
                ) {
                    StyledTextField(
                        text = viewModel.insertedText,
                        onValueChange = {
                            viewModel.insertedText = it
                            viewModel.enqueueTranslation()
                        },
                        maxLines = 8,
                        placeholder = stringResource(R.string.enter_text),
                        modifier = Modifier.focusRequester(focusRequester)
                    )

                    Divider(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                            .size(70.dp, 1.dp)
                    )

                    val clipboardHelper = ClipboardHelper(
                        LocalContext.current.applicationContext
                    )
                    val copiedText = clipboardHelper.get()

                    if (copiedText != null && viewModel.insertedText == "") {
                        Button(
                            onClick = {
                                viewModel.insertedText = copiedText
                                viewModel.enqueueTranslation()
                            },
                            modifier = Modifier
                                .padding(15.dp, 0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ContentPaste,
                                    null,
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                                Text(
                                    stringResource(
                                        id = R.string.paste
                                    ),
                                    modifier = Modifier
                                        .padding(6.dp, 0.dp, 0.dp, 0.dp)
                                )
                            }
                        }
                    }

                    StyledTextField(
                        text = viewModel.translatedText,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .weight(1.0f)
                    )

                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .size(70.dp, 2.dp)
                    )
                }

                if (viewModel.insertedText != "") {
                    ExtendedFloatingActionButton(
                        onClick = {
                            viewModel.insertedText = ""
                            viewModel.translatedText = ""
                            focusRequester.requestFocus()
                        },
                        modifier = Modifier.align(
                            alignment = Alignment.BottomEnd
                        )
                            .padding(15.dp, 40.dp)
                    ) {
                        Row {
                            Icon(
                                Icons.Default.Add,
                                null
                            )
                            Spacer(
                                Modifier.width(10.dp)
                            )

                            Text(
                                stringResource(R.string.new_translation),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageSelector(
                viewModel.availableLanguages,
                viewModel.sourceLanguage,
                autoLanguageEnabled = true
            ) {
                viewModel.sourceLanguage = it
            }

            IconButton(
                onClick = {
                    if (viewModel.sourceLanguage.code == "auto") return@IconButton
                    val temp = viewModel.sourceLanguage
                    viewModel.sourceLanguage = viewModel.targetLanguage
                    viewModel.targetLanguage = temp
                }
            ) {
                Icon(
                    painterResource(R.drawable.ic_switch),
                    null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = if (viewModel.sourceLanguage.code == "auto") {
                        Color.Gray
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
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

@Preview
@Composable
fun MainContentPreview() {
    MainContent()
}

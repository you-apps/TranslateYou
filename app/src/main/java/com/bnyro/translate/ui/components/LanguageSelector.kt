/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.translate.DatabaseHolder
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ui.dialogs.FullscreenDialog
import com.bnyro.translate.ui.models.TranslationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LanguageSelector(
    availableLanguages: List<Language>,
    selectedLanguage: Language,
    autoLanguageEnabled: Boolean = false,
    viewModel: TranslationModel,
    useElevatedButton: Boolean = true,
    onClick: (Language) -> Unit
) {
    val scope = rememberCoroutineScope()

    var showDialog by remember {
        mutableStateOf(false)
    }
    val lazyListState = rememberLazyListState()

    var searchQuery by remember {
        mutableStateOf("")
    }

    val bookmarks = viewModel.bookmarkedLanguages.filter {
        validateFilter(it, searchQuery)
    }

    LaunchedEffect(Unit, availableLanguages) {
        // automatically scroll to the currently selected language
        var index = bookmarks.indexOfFirst { it == selectedLanguage }
        if (index == -1) index = bookmarks.size + availableLanguages.indexOfFirst { it == selectedLanguage }
        if (index != -1) lazyListState.animateScrollToItem(index + 1)
    }

    if (useElevatedButton) {
        ElevatedButton(
            modifier = Modifier
                .padding(5.dp),
            shape = RoundedCornerShape(15.dp),
            onClick = { showDialog = true }
        ) {
            Text(
                modifier = Modifier
                    .basicMarquee()
                    .padding(vertical = 7.dp, horizontal = 10.dp),
                text = selectedLanguage.name,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    } else {
        val interactionSource = remember {
            MutableInteractionSource()
        }

        Text(
            modifier = Modifier
                .basicMarquee()
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    showDialog = true
                }
                .padding(vertical = 7.dp, horizontal = 10.dp),
            text = selectedLanguage.name,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }

    val languages = availableLanguages.toMutableList()

    if (showDialog) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
        )

        FullscreenDialog(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            onDismissRequest = {
                showDialog = false
            },
            topBar = {
                SearchAppBar(
                    title = stringResource(
                        if (autoLanguageEnabled) R.string.translate_from else R.string.translate_to
                    ),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    navigationIcon = {
                        StyledIconButton(
                            imageVector = Icons.Default.ArrowBack,
                            onClick = { showDialog = false }
                        )
                    },
                    actions = {},
                    scrollBehavior = scrollBehavior
                )
            },
            content = {
                LazyColumn(
                    state = lazyListState
                ) {
                    if (autoLanguageEnabled) {
                        item {
                            val autoLanguage = Language("", stringResource(R.string.auto))
                            LanguageItem(
                                language = autoLanguage,
                                isPinned = null,
                                selectedLanguage = selectedLanguage,
                                onClick = {
                                    onClick.invoke(autoLanguage)
                                    showDialog = false
                                },
                                onPinnedChange = {}
                            )
                        }
                    }

                    if (bookmarks.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(
                                    top = 20.dp,
                                    bottom = 10.dp,
                                    start = 15.dp
                                ),
                                text = stringResource(R.string.favorites).uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    items(bookmarks) {
                        LanguageItem(
                            language = it,
                            isPinned = viewModel.bookmarkedLanguages.contains(it),
                            selectedLanguage = selectedLanguage,
                            onClick = {
                                onClick.invoke(it)
                                showDialog = false
                            },
                            onPinnedChange = {
                                viewModel.bookmarkedLanguages =
                                    viewModel.bookmarkedLanguages.filter { language ->
                                        it != language
                                    }
                                scope.launch(Dispatchers.IO) {
                                    DatabaseHolder.Db.languageBookmarksDao().delete(it)
                                }
                            }
                        )
                    }

                    item {
                        Text(
                            modifier = Modifier.padding(
                                top = 20.dp,
                                bottom = 10.dp,
                                start = 15.dp
                            ),
                            text = stringResource(R.string.all_languages).uppercase(),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }

                    items(
                        languages.filter {
                            validateFilter(it, searchQuery)
                        }
                    ) {
                        LanguageItem(
                            language = it,
                            isPinned = viewModel.bookmarkedLanguages.contains(it),
                            selectedLanguage = selectedLanguage,
                            onClick = {
                                onClick.invoke(it)
                                showDialog = false
                            },
                            onPinnedChange = {
                                viewModel.bookmarkedLanguages =
                                    if (viewModel.bookmarkedLanguages.contains(it)) {
                                        scope.launch(Dispatchers.IO) {
                                            DatabaseHolder.Db.languageBookmarksDao().delete(it)
                                        }
                                        viewModel.bookmarkedLanguages - it
                                    } else {
                                        scope.launch(Dispatchers.IO) {
                                            DatabaseHolder.Db.languageBookmarksDao().insertAll(it)
                                        }
                                        viewModel.bookmarkedLanguages + it
                                    }
                            }
                        )
                    }
                }
            }
        )
    }
}

private fun validateFilter(language: Language, query: String): Boolean {
    if (query.isEmpty()) return true

    return language.name.lowercase().contains(query, ignoreCase = true) ||
        language.code.lowercase().contains(query, ignoreCase = true)
}

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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    var showSearchBar by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (showSearchBar) {
                    StyledIconButton(imageVector = Icons.AutoMirrored.Filled.ArrowBack) {
                        showSearchBar = false
                    }
                } else {
                    navigationIcon.invoke()
                }
            },
            actions = {
                StyledIconButton(imageVector = Icons.Default.Search) {
                    showSearchBar = !showSearchBar

                    if (showSearchBar) {
                        scope.launch {
                            // focus can only be requested if the search bar is shown
                            delay(100)
                            if (showSearchBar) focusRequester.requestFocus()
                        }
                    }
                }

                actions.invoke(this)
            }
        )

        AnimatedVisibility(showSearchBar) {
            SearchBar(
                modifier = Modifier.offset(y = (-30).dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        modifier = Modifier.focusRequester(focusRequester),
                        query = value,
                        onQueryChange = onValueChange,
                        placeholder = { Text(stringResource(R.string.search)) },
                        onSearch = {
                            showSearchBar = false
                        },
                        expanded = false,
                        onExpandedChange = {},
                    )
                },
                expanded = false,
                onExpandedChange = {},
                content = {}
            )
        }
    }
}

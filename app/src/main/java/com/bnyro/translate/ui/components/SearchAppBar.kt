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

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    var isSearchViewVisible by remember {
        mutableStateOf(false)
    }

    LargeTopAppBar(
        navigationIcon = {
            if (!isSearchViewVisible) navigationIcon()
        },
        scrollBehavior = scrollBehavior,
        title = {
            Text(title)
        },
        actions = {
            Crossfade(isSearchViewVisible, label = "search-bar-crossfade") {
                when (it) {
                    true -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 15.dp)
                        ) {
                            val focusManager = LocalFocusManager.current
                            val focusRequester = remember { FocusRequester() }

                            SideEffect {
                                focusRequester.requestFocus()
                            }
                            StyledIconButton(
                                modifier = Modifier.padding(top = 10.dp),
                                imageVector = Icons.Default.ArrowBack
                            ) {
                                isSearchViewVisible = false
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .weight(1f)
                                    .offset(y = (-4).dp),
                                value = value,
                                onValueChange = onValueChange,
                                label = {
                                    Text(text = stringResource(R.string.search))
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                    }
                                ),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Search, null)
                                }
                            )
                        }
                    }
                    else -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StyledIconButton(imageVector = Icons.Default.Search) {
                                isSearchViewVisible = true
                            }
                            actions.invoke(this)
                        }
                    }
                }
            }
        }
    )
}

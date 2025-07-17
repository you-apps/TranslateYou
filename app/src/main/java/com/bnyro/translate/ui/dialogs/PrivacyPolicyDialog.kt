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

package com.bnyro.translate.ui.dialogs

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.const.AboutLinks
import com.bnyro.translate.ext.setText
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PrivacyPolicyDialog(
    onDismissRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current

    var privacyPolicyHtml by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            privacyPolicyHtml = try {
                URL(AboutLinks.PRIVACY_POLICY).readText()
            } catch (e: Exception) {
                e.localizedMessage ?: ""
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.privacy_policy))
        },
        text = {
            val scrollState = rememberScrollState()
            Text(
                text = privacyPolicyHtml,
                modifier = Modifier
                    .verticalScroll(scrollState),
                minLines = 20
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                }
            ) {
                Text(stringResource(R.string.okay))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        clipboard.setText(privacyPolicyHtml)
                    }
                    onDismissRequest.invoke()
                }
            ) {
                Text(stringResource(R.string.copy))
            }
        }
    )
}

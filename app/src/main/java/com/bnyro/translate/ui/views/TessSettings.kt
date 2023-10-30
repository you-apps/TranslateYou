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

package com.bnyro.translate.ui.views

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.obj.TessLanguage
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.dialogs.FullscreenDialog
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TessHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TessSettings(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    var availableLanguages by remember {
        mutableStateOf(emptyList<TessLanguage>())
    }

    var downloadedLanguages by remember {
        mutableStateOf(TessHelper.getDownloadedLanguages(context))
    }

    var selectedLanguage by remember {
        mutableStateOf(Preferences.get(Preferences.tessLanguageKey, ""))
    }

    val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Refresh the downloaded languages every time a download finishes
            downloadedLanguages = TessHelper.getDownloadedLanguages(context)
        }
    }

    DisposableEffect(Unit) {
        context.registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        onDispose {
            context.unregisterReceiver(onDownloadComplete)
        }
    }

    LaunchedEffect(Unit) {
        availableLanguages = withContext(Dispatchers.IO) {
            TessHelper.getAvailableLanguages()
        }
    }

    FullscreenDialog(
        onDismissRequest = onDismissRequest,
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.image_translation)) },
                navigationIcon = {
                    StyledIconButton(imageVector = Icons.Default.ArrowBack, onClick = onDismissRequest)
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SelectionContainer(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(text = stringResource(R.string.tess_summary, TessHelper.tessRepoUrl))
                }
                Spacer(modifier = Modifier.height(12.dp))

                // downloaded languages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(downloadedLanguages) {
                        TessSettingsRow(
                            packName = "$it${TessHelper.DATA_FILE_SUFFIX}",
                            size = null,
                            selectedLanguage = selectedLanguage,
                            onSelect = { selectedLanguage = it }
                        ) {
                            StyledIconButton(imageVector = Icons.Default.Delete) {
                                if (TessHelper.deleteLanguage(context, it)) {
                                    downloadedLanguages = TessHelper.getDownloadedLanguages(context)
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.unknown_error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .size(70.dp, 1.dp)
                )
                // not yet downloaded languages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val notYetDownloadedLanguages = availableLanguages.filter { tessLang ->
                        downloadedLanguages.none {
                            tessLang.path.replace(TessHelper.DATA_FILE_SUFFIX, "") == it
                        }
                    }

                    items(notYetDownloadedLanguages) {
                        TessSettingsRow(
                            packName = it.path,
                            size = it.size,
                            selectedLanguage = selectedLanguage
                        ) {
                            var downloading by remember {
                                mutableStateOf(false)
                            }
                            if (!downloading) {
                                StyledIconButton(imageVector = Icons.Default.Download) {
                                    TessHelper.downloadLanguageData(context, it.path)
                                    downloading = true
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        downloading = false
                                    }, 2000)
                                }
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .requiredSize(27.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    }
                }

                DialogButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    text = stringResource(R.string.okay)
                ) {
                    onDismissRequest.invoke()
                }
            }
        }
    )
}

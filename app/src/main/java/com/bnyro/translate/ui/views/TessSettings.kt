package com.bnyro.translate.ui.views

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TessHelper

@Composable
fun TessSettings(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    var downloadedLanguages by remember {
        mutableStateOf(TessHelper.getAvailableLanguages(context))
    }

    var selectedLanguage by remember {
        mutableStateOf(Preferences.get(Preferences.tessLanguageKey, ""))
    }

    val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Refresh the downloaded languages every time a download finishes
            downloadedLanguages = TessHelper.getAvailableLanguages(context)
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

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.image_translation))
        },
        confirmButton = {
            DialogButton(stringResource(R.string.okay)) {
                onDismissRequest.invoke()
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // downloaded languages
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(downloadedLanguages) {
                        Card(
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(30.dp)
                                )
                                .clickable {
                                    Preferences.put(Preferences.tessLanguageKey, it)
                                    selectedLanguage = it
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            )

                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (selectedLanguage == it) "$it   ✓" else it,
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .weight(1f)
                                )
                                StyledIconButton(imageVector = Icons.Default.Delete) {
                                    if (TessHelper.deleteLanguage(context, it)) {
                                        downloadedLanguages = TessHelper.getAvailableLanguages(
                                            context
                                        )
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        TessHelper.availableLanguages.filter {
                            !downloadedLanguages.contains(it)
                        }
                    ) {
                        Card(
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(30.dp)
                                )
                                .clickable { },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            )

                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = it,
                                    modifier = Modifier
                                        .padding(15.dp)
                                )
                                var downloading by remember {
                                    mutableStateOf(false)
                                }
                                if (!downloading) {
                                    StyledIconButton(imageVector = Icons.Default.Download) {
                                         TessHelper.downloadLanguageData(context, it)
                                        downloading = true
                                    }
                                } else {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(10.dp).requiredSize(27.dp),
                                        strokeWidth = 3.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

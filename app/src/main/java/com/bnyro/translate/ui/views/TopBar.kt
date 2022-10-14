package com.bnyro.translate.ui.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.TopBarMenu
import com.bnyro.translate.ui.models.MainModel
import com.bnyro.translate.util.ClipboardHelper
import com.bnyro.translate.util.SpeechHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    mainModel: MainModel = viewModel(),
    menuItems: List<MenuItemData>
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        },
        actions = {
            if (mainModel.insertedText == "" && SpeechRecognizer.isRecognitionAvailable(context)) {
                StyledIconButton(
                    imageVector = Icons.Default.Mic
                ) {
                    if (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        SpeechHelper.checkPermission(context as Activity)
                        return@StyledIconButton
                    }

                    SpeechHelper.recognizeSpeech(context as Activity) {
                        mainModel.insertedText = it
                        mainModel.enqueueTranslation()
                    }
                }
            }

            if (mainModel.translatedText != "") {
                StyledIconButton(
                    imageVector = Icons.Default.ContentCopy,
                    onClick = {
                        ClipboardHelper(
                            context
                        ).write(
                            mainModel.translatedText
                        )
                    }
                )
            }

            if (mainModel.translatedText != "") {
                StyledIconButton(
                    imageVector = Icons.Default.Share,
                    onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, mainModel.translatedText)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(shareIntent)
                    }
                )
            }

            if (mainModel.insertedText != "") {
                StyledIconButton(
                    imageVector = Icons.Default.Clear,
                    onClick = {
                        mainModel.clearTranslation()
                    }
                )
            }

            TopBarMenu(menuItems)
        }
    )
}

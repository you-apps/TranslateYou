package com.bnyro.translate.ui.views

import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.ui.components.ButtonWithIcon
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.StyledTextField
import com.bnyro.translate.ui.models.MainModel
import com.bnyro.translate.util.ClipboardHelper
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.SimTranslationComponent
import com.bnyro.translate.util.SpeechHelper
import kotlinx.coroutines.launch

@Composable
fun TranslationComponent() {
    val viewModel: MainModel = viewModel()
    val context = LocalContext.current
    val view = LocalView.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val clipboardHelper = ClipboardHelper(
        LocalContext.current.applicationContext
    )
    var hasClip by remember {
        mutableStateOf(
            clipboardHelper.hasClip()
        )
    }

    var isKeyboardOpen by remember {
        mutableStateOf(false)
    }
    // detect whether the keyboard is open or closed
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1.0f)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                StyledTextField(
                    text = viewModel.insertedText,
                    placeholder = stringResource(R.string.enter_text)
                ) {
                    viewModel.insertedText = it
                    if (it == "") hasClip = clipboardHelper.hasClip()
                    viewModel.enqueueTranslation()
                }

                val modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)

                if (viewModel.translating) {
                    LinearProgressIndicator(
                        modifier = modifier
                    )
                } else {
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = modifier
                            .size(70.dp, 1.dp)
                    )
                }

                if (viewModel.translation.translatedText != "" && SpeechHelper.ttsAvailable) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        StyledIconButton(
                            imageVector = Icons.Default.VolumeUp
                        ) {
                            SpeechHelper.speak(
                                context,
                                viewModel.translation.translatedText,
                                viewModel.targetLanguage.code
                            )
                        }
                    }
                }

                if (hasClip && viewModel.insertedText.isBlank()) {
                    Row {
                        ButtonWithIcon(
                            text = stringResource(R.string.paste),
                            icon = Icons.Default.ContentPaste
                        ) {
                            viewModel.insertedText = clipboardHelper.get() ?: ""
                            viewModel.enqueueTranslation()
                        }

                        Spacer(
                            modifier = Modifier
                                .width(0.dp)
                        )

                        ButtonWithIcon(
                            text = stringResource(R.string.forget),
                            icon = Icons.Default.Clear
                        ) {
                            clipboardHelper.clear()
                            hasClip = false
                            viewModel.clearTranslation()
                        }
                    }
                } else if (
                    viewModel.insertedText.isNotBlank() &&
                    !Preferences.get(Preferences.translateAutomatically, true)
                ) {
                    ButtonWithIcon(
                        text = stringResource(R.string.translate),
                        icon = Icons.Default.Translate
                    ) {
                        viewModel.translate()
                    }
                }

                val charPref = Preferences.get(Preferences.charCounterLimitKey, "")

                StyledTextField(
                    text = viewModel.translation.translatedText,
                    readOnly = true,
                    textColor = if (
                        charPref != "" && viewModel.translation.translatedText.length >= charPref.toInt()
                    ) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.typography.bodyMedium.color
                    }
                )
            }

            if (scrollState.value > 100) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowUpward, null)
                }
            }
        }

        if (Preferences.get(
                Preferences.showAdditionalInfo,
                true
            ) && !isKeyboardOpen
        ) {
            AdditionalInfoComponent(viewModel.translation)
        }

        Spacer(
            modifier = Modifier
                .height(15.dp)
        )

        if (viewModel.simTranslationEnabled) {
            SimTranslationComponent()
        } else {
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .size(70.dp, 2.dp)
            )
        }
    }
}

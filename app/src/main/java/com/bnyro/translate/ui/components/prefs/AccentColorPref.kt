package com.bnyro.translate.ui.components.prefs

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.bnyro.translate.R
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ui.MainActivity
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.theme.defaultAccentColor
import com.bnyro.translate.util.Preferences
import okhttp3.internal.toHexString

@Composable
fun AccentColorPrefDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val supportsDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    var color by remember {
        mutableStateOf(
            Preferences.getAccentColor() ?: run {
                if (supportsDynamicColors) null else defaultAccentColor
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DialogButton(text = stringResource(R.string.okay)) {
                Preferences.prefs.edit(true) { putString(Preferences.accentColorKey, color) }
                (context as MainActivity).accentColor = color
                onDismissRequest.invoke()
            }
        },
        dismissButton = {
            DialogButton(text = stringResource(R.string.cancel)) {
                onDismissRequest.invoke()
            }
        },
        title = {
            Text(stringResource(R.string.accent_color))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (supportsDynamicColors) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.dynamic_colors)
                        )
                        Switch(
                            checked = color == null,
                            onCheckedChange = { newValue ->
                                color = defaultAccentColor.takeIf { !newValue }
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier.height(250.dp)
                ) {
                    AnimatedVisibility(
                        visible = color != null
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            listOf("R", "G", "B").forEachIndexed { index, c ->
                                val startIndex = index * 2
                                color?.let {
                                    ColorSlider(
                                        label = c,
                                        value = it.substring(startIndex, startIndex + 2).toInt(16),
                                        onChange = { colorInt ->
                                            var newHex = colorInt.toHexString()
                                            if (newHex.length == 1) newHex = "0$newHex"
                                            color = StringBuilder(it).apply {
                                                setCharAt(startIndex, newHex[0])
                                                setCharAt(startIndex + 1, newHex[1])
                                            }.toString()
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            color?.let {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        Modifier.size(50.dp).background(
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                    )
                                    Text(text = "   =>   ", fontSize = 27.sp)
                                    Box(
                                        modifier = Modifier.size(50.dp).background(
                                            it.hexToColor(),
                                            CircleShape
                                        )
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

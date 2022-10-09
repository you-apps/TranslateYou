package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.translate.R
import com.bnyro.translate.util.Preferences

@Composable
fun SliderPreference(
    preferenceKey: String,
    preferenceTitle: String,
    defaultValue: Float,
    minValue: Float,
    maxValue: Float,
    steps: Int
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Text(
        preferenceTitle,
        fontSize = 18.sp,
        modifier = Modifier
            .clickable {
                showDialog = true
            }
            .fillMaxWidth()
            .padding(10.dp, 20.dp)
    )

    if (showDialog) {
        SliderPreferenceDialog(
            title = preferenceTitle,
            preferenceKey = preferenceKey,
            onDismissRequest = {
                showDialog = false
            },
            defaultValue = defaultValue,
            minValue = minValue,
            maxValue = maxValue,
            steps = steps
        )
    }
}

@Composable
fun SliderPreferenceDialog(
    title: String,
    preferenceKey: String,
    onDismissRequest: () -> Unit,
    defaultValue: Float,
    minValue: Float,
    maxValue: Float,
    steps: Int

) {
    var value by remember {
        mutableStateOf(
            Preferences.get(
                preferenceKey,
                defaultValue
            )
        )
    }

    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Slider(
                value = value,
                onValueChange = {
                    value = it
                },
                valueRange = minValue..maxValue,
                steps = steps
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    Preferences.put(
                        preferenceKey,
                        value
                    )
                    onDismissRequest.invoke()
                }
            ) {
                Text(
                    stringResource(R.string.okay)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    stringResource(R.string.cancel)
                )
            }
        }
    )
}

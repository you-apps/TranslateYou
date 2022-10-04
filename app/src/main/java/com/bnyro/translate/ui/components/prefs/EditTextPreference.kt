package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bnyro.translate.util.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextPreference(
    preferenceKey: String,
    defaultValue: String,
    labelText: String,
    onValueChange: (String) -> Unit = {}
) {
    var insertedText by remember {
        mutableStateOf(
            defaultValue
        )
    }

    OutlinedTextField(
        value = insertedText,
        onValueChange = {
            insertedText = it

            Preferences.put(preferenceKey, it)

            onValueChange.invoke(it)
        },
        label = {
            Text(
                text = labelText
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

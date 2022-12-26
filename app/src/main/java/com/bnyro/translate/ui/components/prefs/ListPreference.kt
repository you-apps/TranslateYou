package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.ui.components.ListPreferenceDialog
import com.bnyro.translate.util.Preferences

@Composable
fun ListPreference(
    title: String,
    summary: String? = null,
    preferenceKey: String,
    defaultValue: String,
    entries: List<String>,
    values: List<String>,
    onOptionSelected: (ListPreferenceOption) -> Unit = {}
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    var summaryIndex by remember {
        mutableStateOf(values.indexOf(Preferences.get(preferenceKey, defaultValue)))
    }
    PreferenceItem(
        title = title,
        summary = summary ?: entries.getOrElse(summaryIndex) { entries.first() },
        modifier = Modifier.fillMaxWidth()
    ) {
        showDialog = true
    }

    if (showDialog) {
        ListPreferenceDialog(
            preferenceKey = preferenceKey,
            onDismissRequest = {
                showDialog = false
            },
            options = entries.mapIndexed { index, entry ->
                ListPreferenceOption(entry, index)
            },
            onOptionSelected = {
                Preferences.put(preferenceKey, values[it.value])
                summaryIndex = it.value
                onOptionSelected.invoke(it)
            }
        )
    }
}

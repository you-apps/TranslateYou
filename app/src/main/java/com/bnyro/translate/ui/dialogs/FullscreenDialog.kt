package com.bnyro.translate.ui.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun FullscreenDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            modifier = modifier,
            topBar = topBar
        ) { pV ->
            Box(Modifier.padding(pV)) {
                content.invoke()
            }
        }
    }
}

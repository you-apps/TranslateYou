package com.bnyro.translate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(modifier: Modifier = Modifier, readOnly: Boolean = false) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
        },
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StyledTextField()
}

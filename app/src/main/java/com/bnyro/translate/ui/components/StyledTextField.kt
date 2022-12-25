package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 10,
    placeholder: String? = null,
    readOnly: Boolean = false,
    fontSize: TextUnit = 23.sp,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = {
            onValueChange.invoke(it)
        },
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent
        ),
        placeholder = {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    fontSize = fontSize
                )
            }
        },
        textStyle = TextStyle(
            fontSize = fontSize
        ),
        maxLines = maxLines
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    StyledTextField(text = "") {}
}

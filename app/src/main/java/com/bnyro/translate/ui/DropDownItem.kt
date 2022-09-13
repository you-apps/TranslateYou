package com.bnyro.translate.ui.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.OptionsDialog

@Composable
fun DropDownItem(menuItemData: MenuItemData, updateExpanded: (newState: Boolean) -> Unit) {
    val openDialog = remember {
        mutableStateOf(false)
    }

    DropdownMenuItem(
        onClick = {
            openDialog.value = true
            updateExpanded.invoke(false)
        },
        enabled = true,
        text = {
            Row {
                Icon(
                    imageVector = menuItemData.icon,
                    contentDescription = menuItemData.text
                )

                Spacer(modifier = Modifier.width(width = 8.dp))

                Text(
                    text = menuItemData.text,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    )

    if (openDialog.value) {
        OptionsDialog(
            onDismissRequest = {
                openDialog.value = false
            }
        )
    }
}

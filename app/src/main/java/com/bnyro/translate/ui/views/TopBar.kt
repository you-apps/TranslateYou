package com.bnyro.translate.ui.theme

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.ext.getAppName
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.obj.MenuItemData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    mainModel: MainModel = viewModel()
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val menuItemsList: ArrayList<MenuItemData> = arrayListOf(
        MenuItemData(
            text = stringResource(
                id = R.string.options
            ),
            icon = Icons.Outlined.Menu
        )
    )

    TopAppBar(
        title = {
            Text(
                LocalContext.current.applicationContext.getAppName()
            )
        },
        actions = {
            if (mainModel.insertedText != "") {
                IconButton(
                    onClick = {
                        mainModel.insertedText = ""
                        mainModel.translatedText = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        null
                    )
                }
            }
            // 3 vertical dots icon
            IconButton(
                onClick = {
                    expanded = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    null
                )
            }

            DropdownMenu(
                modifier = Modifier.width(width = 150.dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                // adjust the position
                offset = DpOffset(x = (-102).dp, y = (-64).dp),
                properties = PopupProperties()
            ) {
                // adding each menu item
                menuItemsList.forEach { menuItemData ->
                    DropDownItem(menuItemData) { newState ->
                        expanded = newState
                    }
                }
            }
        }
    )
}

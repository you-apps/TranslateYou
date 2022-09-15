package com.bnyro.translate.ui.views

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bnyro.translate.models.MainModel
import com.bnyro.translate.models.NavigationModel
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.components.DropDownItem
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.util.ClipboardHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    mainModel: MainModel = viewModel(),
    navigationModel: NavigationModel = viewModel()
) {
    val context = LocalContext.current.applicationContext

    var expanded by remember {
        mutableStateOf(false)
    }

    val menuItemsList: ArrayList<MenuItemData> = arrayListOf(
        MenuItemData(
            text = stringResource(
                id = R.string.options
            ),
            icon = Icons.Default.Menu
        ) {
            navigationModel.showOptions = true
        },
        MenuItemData(
            text = stringResource(
                id = R.string.about
            ),
            icon = Icons.Default.Info
        ) {
            navigationModel.showAbout = true
        }
    )

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        },
        actions = {
            if (mainModel.translatedText != "") {
                StyledIconButton(
                    imageVector = Icons.Default.ContentCopy,
                    onClick = {
                        ClipboardHelper(
                            context
                        ).write(
                            mainModel.translatedText
                        )
                    }
                )
            }

            if (mainModel.insertedText != "") {
                StyledIconButton(
                    imageVector = Icons.Default.Clear,
                    onClick = {
                        mainModel.insertedText = ""
                        mainModel.translatedText = ""
                    }
                )
            }
            // 3 vertical dots icon
            StyledIconButton(
                imageVector = Icons.Default.MoreVert,
                onClick = {
                    expanded = true
                }
            )

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

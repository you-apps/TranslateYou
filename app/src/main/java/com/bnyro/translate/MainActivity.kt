package com.bnyro.translate

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.bnyro.translate.ui.theme.TranslateYouTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslateYouTheme {
                ScreenContent()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar()
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val listItems = getMenuItemsList()

    var expanded by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Text("Translate You")
        },
        actions = {
            // 3 vertical dots icon
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open Options"
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
                listItems.forEach { menuItemData ->
                    DropDownItem(menuItemData) { newState ->
                        expanded = newState
                    }
                }
            }
        }
    )
}

@Composable
fun DropDownItem(menuItemData: MenuItemData, updateExpanded: (newState: Boolean) -> Unit) {
    val iconAndTextColor: Color = Color.DarkGray
    val context = LocalContext.current.applicationContext

    DropdownMenuItem(
        onClick = {
            Toast.makeText(context, menuItemData.text, Toast.LENGTH_SHORT)
                .show()
            updateExpanded.invoke(false)
        },
        enabled = true,
        text = {
            Row {
                Icon(
                    imageVector = menuItemData.icon,
                    contentDescription = menuItemData.text,
                    tint = iconAndTextColor
                )

                Spacer(modifier = Modifier.width(width = 8.dp))

                Text(
                    text = menuItemData.text,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = iconAndTextColor
                )
            }
        }
    )
}

fun getMenuItemsList(): ArrayList<MenuItemData> = arrayListOf(
    MenuItemData(
        text = "Options",
        icon = Icons.Outlined.Menu
    )
)

data class MenuItemData(
    val text: String,
    val icon: ImageVector
)

@Composable
fun MainContent() {
    Text(text = "UI Placeholder")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ScreenContent()
}

package com.bnyro.translate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledScaffold(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    topBarTonalElevation: Dp = 0.dp,
    containerTonalElevation: Dp = 0.dp,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(
                    topBarTonalElevation
                )
            )
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
            containerTonalElevation
        ),
        topBar = {
            if (navigationIcon != null || actions != null) {
                SmallTopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            topBarTonalElevation
                        )
                    ),
                    navigationIcon = { navigationIcon?.invoke() },
                    actions = { actions?.invoke(this) }
                )
            }
        },
        content = {
            Column {
                Spacer(modifier = Modifier.height(it.calculateTopPadding()))
                content()
            }
        },
        bottomBar = { bottomBar?.invoke() },
        floatingActionButton = { floatingActionButton?.invoke() }
    )
}

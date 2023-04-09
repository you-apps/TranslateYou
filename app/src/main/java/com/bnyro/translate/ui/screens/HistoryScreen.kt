package com.bnyro.translate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.components.SearchAppBar
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.TopBarMenu
import com.bnyro.translate.ui.models.HistoryModel
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.views.HistoryRow

@Composable
fun HistoryScreen(
    navController: NavController,
    translationModel: TranslationModel
) {
    val viewModel: HistoryModel = viewModel()

    var searchQuery by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            SearchAppBar(
                title = stringResource(R.string.history),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack
                    ) {
                        navController.popBackStack()
                    }
                },
                actions = {
                    TopBarMenu(
                        menuItems = listOf(
                            MenuItemData(
                                stringResource(id = R.string.clear_history),
                                Icons.Default.Delete
                            ) {
                                viewModel.clearHistory()
                            }
                        )
                    )
                }
            )
        },
        content = { pV ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pV)
            ) {
                val query = searchQuery.lowercase()
                val filteredHistory = viewModel.history.filter {
                    it.insertedText.lowercase().contains(query) ||
                        it.translatedText.lowercase().contains(query)
                }

                if (filteredHistory.isNotEmpty()) {
                    LazyColumn {
                        items(filteredHistory) {
                            HistoryRow(navController, translationModel, it) {
                                viewModel.history = viewModel.history.filter { item ->
                                    it.id != item.id
                                }
                                viewModel.deleteHistoryItem(it)
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(100.dp),
                            imageVector = Icons.Default.History,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = stringResource(R.string.nothing_here),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    )
}

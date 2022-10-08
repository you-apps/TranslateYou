package com.bnyro.translate.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.TopBarMenu
import com.bnyro.translate.ui.models.HistoryModel

class HistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[HistoryModel::class.java]
        viewModel.fetchHistory()

        Content {
            HistoryContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryContent(
    viewModel: HistoryModel = viewModel()
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.history)
                    )
                },
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack
                    ) {
                        (context as Activity).finish()
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
            Column(
                modifier = Modifier.padding(pV)
            ) {
                LazyColumn {
                    items(viewModel.history) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // TODO() Start Main Activity with data
                                }
                                .padding(
                                    start = 15.dp,
                                    end = 5.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1.0f)

                            ) {
                                Text(
                                    it.insertedText,
                                    fontSize = 18.sp
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(5.dp)
                                )

                                Text(
                                    it.translatedText,
                                    fontSize = 14.sp
                                )
                            }
                            StyledIconButton(
                                imageVector = Icons.Default.Delete
                            ) {
                                viewModel.history = viewModel.history.filter { item ->
                                    it.id != item.id
                                }

                                viewModel.deleteHistoryItem(it)
                            }
                        }
                    }
                }
            }
        }
    )
}

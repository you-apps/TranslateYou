package com.bnyro.translate.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.ext.parcelable
import com.bnyro.translate.ext.startActivity
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.base.BaseActivity
import com.bnyro.translate.ui.components.LanguageSelector
import com.bnyro.translate.ui.models.MainModel
import com.bnyro.translate.ui.views.TopBar
import com.bnyro.translate.ui.views.TranslationComponent
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.Preferences
import kotlinx.serialization.encodeToString

class MainActivity : BaseActivity() {
    private lateinit var mainModel: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mainModel = ViewModelProvider(this)[MainModel::class.java]

        super.onCreate(savedInstanceState)

        Content {
            ScreenContent()
        }

        handleIntentData()
    }

    override fun onStop() {
        Preferences.put(
            Preferences.sourceLanguage,
            JsonHelper.json.encodeToString(mainModel.sourceLanguage)
        )
        Preferences.put(
            Preferences.targetLanguage,
            JsonHelper.json.encodeToString(mainModel.targetLanguage)
        )
        super.onStop()
    }

    private fun getIntentText(): String? {
        intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.let {
            return it.toString()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.let {
                return it.toString()
            }
        }
        intent.getCharSequenceExtra(Intent.ACTION_SEND)?.let {
            return it.toString()
        }
        return null
    }

    override fun onStart() {
        super.onStart()

        mainModel.refresh()

        mainModel.fetchLanguages {
            Toast.makeText(
                this,
                R.string.server_error,
                Toast.LENGTH_LONG
            ).show()
        }

        mainModel.fetchBookmarkedLanguages()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        handleIntentData()
    }

    private fun handleIntentData() {
        getIntentText()?.let {
            mainModel.insertedText = it
            mainModel.translate()
        }
        if (intent.type?.startsWith("image/") != true) return

        (intent.parcelable<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            mainModel.processImage(this, it)
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent() {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                menuItems = listOf(
                    MenuItemData(
                        text = stringResource(
                            id = R.string.options
                        ),
                        icon = Icons.Default.Menu
                    ) {
                        context.startActivity(
                            SettingsActivity::class.java
                        )
                    },
                    MenuItemData(
                        text = stringResource(
                            id = R.string.history
                        ),
                        icon = Icons.Default.History
                    ) {
                        context.startActivity(
                            HistoryActivity::class.java
                        )
                    },
                    MenuItemData(
                        text = stringResource(
                            id = R.string.about
                        ),
                        icon = Icons.Default.Info
                    ) {
                        context.startActivity(
                            AboutActivity::class.java
                        )
                    }
                )
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val viewModel: MainModel = viewModel()

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .weight(1.0f)
                ) {
                    TranslationComponent()
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LanguageSelector(
                        viewModel.availableLanguages,
                        viewModel.sourceLanguage,
                        autoLanguageEnabled = true
                    ) {
                        viewModel.sourceLanguage = it
                    }

                    val switchBtnEnabled by mutableStateOf(
                        viewModel.sourceLanguage.code != ""
                    )

                    IconButton(
                        onClick = {
                            if (viewModel.availableLanguages.isEmpty()) return@IconButton
                            if (!switchBtnEnabled) return@IconButton
                            val temp = viewModel.sourceLanguage
                            viewModel.sourceLanguage = viewModel.targetLanguage
                            viewModel.targetLanguage = temp
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_switch),
                            null,
                            modifier = Modifier
                                .size(18.dp),
                            tint = if (switchBtnEnabled) MaterialTheme.colorScheme.onSurface else Color.Gray
                        )
                    }

                    LanguageSelector(
                        viewModel.availableLanguages,
                        viewModel.targetLanguage
                    ) {
                        viewModel.targetLanguage = it
                    }
                }
            }
        }
    }
}

package com.bnyro.translate.ui.views

import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.BuildConfig
import com.bnyro.translate.R
import com.bnyro.translate.models.NavigationModel
import com.bnyro.translate.obj.AboutIcon
import com.bnyro.translate.ui.components.RoundIconButton
import com.bnyro.translate.ui.components.StyledIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(
    viewModel: NavigationModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val view = LocalView.current
    var currentVersion by remember { mutableStateOf("") }
    var clickTime by remember { mutableStateOf(System.currentTimeMillis() - 2000) }

    LaunchedEffect(Unit) {
        currentVersion = BuildConfig.VERSION_CODE.toString()
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Rounded.ArrowBack
                    ) {
                        viewModel.showAbout = false
                    }
                },
                actions = {
                    StyledIconButton(
                        imageVector = Icons.Rounded.Balance
                    )
                }
            )
        },
        content = {
            Column {
                Spacer(modifier = Modifier.height(it.calculateTopPadding()))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    item {
                        Column(
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                        tryAwaitRelease()
                                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                        view.playSoundEffect(SoundEffectConstants.CLICK)
                                    },
                                    onTap = {
                                        clickTime = if (System.currentTimeMillis() - clickTime > 2000) {
                                            System.currentTimeMillis()
                                        } else {
                                            System.currentTimeMillis()
                                        }
                                    }
                                )
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(240.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(30.dp)
                                    )
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(30.dp),
                                        ambientColor = MaterialTheme.colorScheme.primaryContainer,
                                        spotColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    modifier = Modifier.size(90.dp),
                                    painter = painterResource(R.mipmap.ic_launcher),
                                    contentDescription = stringResource(R.string.app_name),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                )
                            }
                            Spacer(modifier = Modifier.height(48.dp))
                            BadgedBox(
                                badge = {
                                    Badge(
                                        modifier = Modifier.animateContentSize(tween(800)),
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        contentColor = MaterialTheme.colorScheme.tertiary
                                    ) {
                                        Text(text = currentVersion)
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.displaySmall
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            aboutIcons.forEach {
                                RoundIconButton(
                                    backgroundColor = MaterialTheme.colorScheme.onTertiary,
                                    contentDescription = it.contentDescription,
                                    iconResourceId = it.iconResourceId
                                ) {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(it.href)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

val aboutIcons = listOf(
    AboutIcon(
        "",
        R.drawable.ic_switch,
        ""
    )
)

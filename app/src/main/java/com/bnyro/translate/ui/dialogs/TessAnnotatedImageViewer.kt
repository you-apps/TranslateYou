/*
 * Copyright (c) 2026 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.ui.dialogs

import androidx.annotation.ColorInt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.bnyro.translate.R
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.models.AnnotatedBitmap
import com.bnyro.translate.util.ImageHelper

private data class RectMetadata(
    val text: String,
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float,
    @ColorInt val background: Int,
    @ColorInt val foreground: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TessAnnotatedImageViewer(
    annotatedBitmap: AnnotatedBitmap,
    isLoading: Boolean,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    FullscreenDialog(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.image_translation))
                },
                actions = {
                    StyledIconButton(imageVector = Icons.Default.Cancel) {
                        onDismissRequest.invoke()
                    }

                    StyledIconButton(imageVector = Icons.Default.Done) {
                        onConfirmRequest.invoke()
                        onDismissRequest.invoke()
                    }
                }
            )
        },
        onDismissRequest = onDismissRequest
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                )
            }

            val textMeasurer = rememberTextMeasurer()
            val maxWidth = with(LocalDensity.current) {
                maxWidth.toPx()
            }
            val maxHeight = with(LocalDensity.current) {
                maxHeight.toPx()
            }
            val (imageWidth, imageHeight) = remember(annotatedBitmap) {
                ImageHelper.fitImageSizeToDimensions(annotatedBitmap.image, maxWidth, maxHeight)
            }

            val density = LocalDensity.current
            fun Int.pxToTextSp() = with(density) {
                this@pxToTextSp.toDp().toSp()
            }

            // cache inside remember scope so that this heavy work isn't done for each frame
            val rectMetadatas = remember(annotatedBitmap.components) {
                annotatedBitmap.components.map { (rect, text) ->
                    val left = rect.left.toFloat() / annotatedBitmap.image.width * imageWidth
                    val top = rect.top.toFloat() / annotatedBitmap.image.height * imageHeight
                    val width =
                        rect.width().toFloat() / annotatedBitmap.image.width * imageWidth
                    val height =
                        rect.height().toFloat() / annotatedBitmap.image.height * imageHeight

                    val backgroundColor =
                        ImageHelper.getBackgroundColorAtRect(annotatedBitmap.image, rect)
                    val foregroundColor =
                        ImageHelper.getHighestContrastColorAtRect(
                            annotatedBitmap.image,
                            rect,
                            backgroundColor
                        )

                    RectMetadata(
                        text = text,
                        left = left,
                        top = top,
                        width = width,
                        height = height,
                        background = backgroundColor,
                        foreground = foregroundColor
                    )
                }
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                // blank space due to unmatching image width / height -> center
                translate(top = (maxHeight - imageHeight) / 2, left = (maxWidth - imageWidth) / 2) {
                    drawImage(
                        annotatedBitmap.image.asImageBitmap(),
                        dstSize = IntSize(imageWidth.toInt(), imageHeight.toInt())
                    )

                    for (rect in rectMetadatas) {
                        translate(left = rect.left, top = rect.top) {
                            val textStyle = TextStyle(
                                fontSize = rect.height.toInt().pxToTextSp(),
                                color = Color(rect.foreground)
                            )
                            val measuredText = textMeasurer.measure(
                                text = rect.text,
                                constraints = Constraints.fixed(
                                    rect.width.toInt(),
                                    rect.height.toInt()
                                ),
                                style = textStyle
                            )
                            // calculate vertical text offset (caused by spacing)
                            val ascent =
                                measuredText.getLineBottom(0) - with(measuredText.layoutInput.density) {
                                    textStyle.fontSize.toPx()
                                }
                            drawRect(Color(rect.background), size = measuredText.size.toSize())
                            translate(top = -ascent) {
                                drawText(measuredText)
                            }
                        }
                    }
                }
            }
        }
    }
}
/*
 * Copyright (c) 2024 You Apps
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

package com.bnyro.translate.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.bnyro.translate.R
import com.bnyro.translate.ext.toastFromMainThread
import com.bnyro.translate.ui.dialogs.FullscreenDialog
import com.bnyro.translate.util.ImageHelper
import com.bnyro.translate.util.ImageTransform
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MAX_FRAME_CLICK_DIST = 70f

private enum class BorderType {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCropDialog(
    bitmap: Bitmap,
    onEditedBitmap: (Bitmap) -> Unit,
    onDismissRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var imageTransform: ImageTransform? = remember { null }

    FullscreenDialog(
        onDismissRequest = onDismissRequest,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.image_translation)) },
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = { onDismissRequest() }
                    )
                },
                actions = {
                    StyledIconButton(
                        imageVector = Icons.Default.Done,
                        onClick = {
                            val currentTransform = imageTransform ?: run {
                                context.toastFromMainThread(R.string.invalid_selection_area)
                                return@StyledIconButton
                            }

                            scope.launch(Dispatchers.IO) {
                                val resizedBitmap =
                                    ImageHelper.cropImage(bitmap, currentTransform)

                                withContext(Dispatchers.Main) {
                                    onEditedBitmap(resizedBitmap)
                                    onDismissRequest()
                                }
                            }
                        }
                    )
                }
            )
        }
    ) {
        CropImageView(
            bitmap = ImageHelper.setAlpha(bitmap, 100),
            onImageTransformChanged = { imageTransform = it }
        )
    }
}

@Composable
fun CropImageView(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    onImageTransformChanged: (transform: ImageTransform?) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val density = LocalDensity.current
        val (maxWidth, maxHeight) = remember {
            with(density) {
                maxWidth.toPx() to maxHeight.toPx()
            }
        }

        var selectableAreaSize by remember {
            val size = Size(maxWidth / 3, maxHeight / 3)
            mutableStateOf(size)
        }

        var selectableAreaOffset by remember {
            val offsetX = (maxWidth - selectableAreaSize.width) / 2
            val offsetY = (maxHeight - selectableAreaSize.height) / 2
            val offset = Offset(offsetX, offsetY)
            mutableStateOf(offset)
        }

        val (imageWidthScaled, imageHeightScaled) = remember {
            fitImageSizeToDimensions(bitmap, maxWidth, maxHeight)
        }

        LaunchedEffect(Unit, selectableAreaSize, selectableAreaOffset) {
            val widthFactor = bitmap.width.toFloat() / imageWidthScaled
            val heightFactor = bitmap.height.toFloat() / imageHeightScaled
            val emptyHorizontalSpace = maxWidth - imageWidthScaled
            val emptyVerticalSpace = maxHeight - imageHeightScaled

            val cropSize = Size(
                selectableAreaSize.width * widthFactor,
                selectableAreaSize.height * heightFactor
            )
            val cropOffset = Offset(
                (selectableAreaOffset.x - emptyHorizontalSpace / 2) * widthFactor,
                (selectableAreaOffset.y - emptyVerticalSpace / 2) * heightFactor
            )
            if (arrayOf(
                    cropSize.height,
                    cropSize.width,
                    cropOffset.x,
                    cropOffset.y
                ).any { it < 0 } || cropOffset.x + cropSize.width > bitmap.width ||
                cropOffset.y + cropSize.height > bitmap.height
            ) {
                onImageTransformChanged(null)
                return@LaunchedEffect
            }

            onImageTransformChanged(
                ImageTransform(
                    cropSize.width.toInt(),
                    cropSize.height.toInt(),
                    cropOffset.x.toInt(),
                    cropOffset.y.toInt()
                )
            )
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val borders = getCloseBorders(change, selectableAreaSize, selectableAreaOffset)
                    if (borders.isNotEmpty()) {
                        selectableAreaSize = Size(
                            if (borders.contains(BorderType.LEFT)) selectableAreaSize.width - dragAmount.x else selectableAreaSize.width + dragAmount.x,
                            if (borders.contains(BorderType.TOP)) selectableAreaSize.height - dragAmount.y else selectableAreaSize.height + dragAmount.y
                        )
                        selectableAreaOffset = Offset(
                            if (borders.contains(BorderType.LEFT)) selectableAreaOffset.x + dragAmount.x else selectableAreaOffset.x,
                            if (borders.contains(BorderType.TOP)) selectableAreaOffset.y + dragAmount.y else selectableAreaOffset.y,
                        )
                    } else if (isInArea(change, selectableAreaSize, selectableAreaOffset)) {
                        selectableAreaOffset = Offset(
                            selectableAreaOffset.x + dragAmount.x,
                            selectableAreaOffset.y + dragAmount.y
                        )
                    }
                }
            }
        ) {
            drawImage(
                bitmap.asImageBitmap(),
                dstOffset = IntOffset(
                    ((maxWidth - imageWidthScaled) / 2).toInt(),
                    ((maxHeight - imageHeightScaled) / 2).toInt()
                ),
                dstSize = IntSize(imageWidthScaled.roundToInt(), imageHeightScaled.roundToInt())
            )

            drawRect(
                color = Color(0x75ffffff),
                size = selectableAreaSize,
                topLeft = selectableAreaOffset
            )

            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            drawRoundRect(
                style = stroke,
                color = Color.White,
                topLeft = selectableAreaOffset,
                size = selectableAreaSize
            )
        }
    }
}

private fun isInArea(
    event: PointerInputChange,
    selectableAreaSize: Size,
    selectableAreaOffset: Offset
): Boolean {
    return event.position.x > selectableAreaOffset.x &&
            event.position.x < selectableAreaOffset.x + selectableAreaSize.width &&
            event.position.y > selectableAreaOffset.y &&
            event.position.y < selectableAreaOffset.y + selectableAreaSize.height
}

private fun getCloseBorders(
    event: PointerInputChange,
    selectableAreaSize: Size,
    selectableAreaOffset: Offset
): Set<BorderType> {
    val borders = mutableSetOf<BorderType>()

    if (diffSmallerThanMaxFrameClickDist(event.position.y, selectableAreaOffset.y)) {
        borders.add(BorderType.TOP)
    }

    if (diffSmallerThanMaxFrameClickDist(
            event.position.y,
            selectableAreaOffset.y + selectableAreaSize.height
        )
    ) {
        borders.add(BorderType.BOTTOM)
    }

    if (diffSmallerThanMaxFrameClickDist(event.position.x, selectableAreaOffset.x)) {
        borders.add(BorderType.LEFT)
    }

    if (diffSmallerThanMaxFrameClickDist(
            event.position.x,
            selectableAreaOffset.x + selectableAreaSize.width
        )
    ) {
        borders.add(BorderType.RIGHT)
    }

    return borders
}

private fun diffSmallerThanMaxFrameClickDist(a: Float, b: Float): Boolean {
    return (a - b).absoluteValue < MAX_FRAME_CLICK_DIST
}

private fun fitImageSizeToDimensions(
    bitmap: Bitmap,
    maxWidth: Float,
    maxHeight: Float
): Pair<Float, Float> {
    val imageRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
    val boxRatio = maxHeight / maxWidth
    return if (imageRatio > boxRatio) {
        maxHeight / imageRatio to maxHeight
    } else {
        maxWidth to maxWidth * imageRatio
    }
}
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

package com.bnyro.translate.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.createBitmap
import kotlin.math.absoluteValue
import androidx.core.graphics.get
import kotlin.math.max
import kotlin.math.min

data class ImageTransform(
    val width: Int,
    val height: Int,
    val offsetX: Int,
    val offsetY: Int
)

object ImageHelper {
    fun getImage(context: Context, uri: Uri): Bitmap? {
        val bitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val `is` = context.contentResolver.openInputStream(uri) ?: return bitmap
            val exifMetadata =
                ExifInterface(`is`).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            return rotateBitmap(bitmap, exifMetadata) ?: bitmap
        }

        return bitmap
    }

    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1F, 1F)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180F)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180F)
                matrix.postScale(-1F, 1F)
            }

            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90F)
                matrix.postScale(-1F, 1F)
            }

            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90F)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90F)
                matrix.postScale(-1F, 1F)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90F)
            else -> return bitmap
        }
        try {
            val bmRotated = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
            bitmap.recycle()
            return bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }
    }

    fun setAlpha(originalBitmap: Bitmap, alpha: Int): Bitmap {
        val newBitmap = createBitmap(originalBitmap.width, originalBitmap.height)
        val canvas = Canvas(newBitmap)
        val paint = Paint().apply { this.alpha = alpha }
        canvas.drawBitmap(originalBitmap, 0f, 0f, paint)
        return newBitmap
    }

    fun cropImage(
        targetBitmap: Bitmap,
        transform: ImageTransform
    ): Bitmap = Bitmap.createBitmap(
        targetBitmap,
        transform.offsetX,
        transform.offsetY,
        transform.width,
        transform.height
    )

    fun fitImageSizeToDimensions(
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

    /**
     * Map similar colors to the same color int.
     */
    fun quantize(color: Int, step: Int = 8): Int {
        val r = ((color shr 16) and 0xFF) / step * step
        val g = ((color shr 8) and 0xFF) / step * step
        val b = (color and 0xFF) / step * step

        return (0xFF shl 24) or (r shl 16) or (g shl 8) or b
    }

    /**
     * Extract the background color at a specific area in a bitmap.
     */
    fun getBackgroundColorAtRect(bitmap: Bitmap, rect: Rect): Int {
        val pixels = mutableListOf<Int>()

        // ensure that all rect measures are inside the bounds of the bitmap
        val fixedLeft = max(0, rect.left)
        val fixedTop = max(0, rect.top)
        val fixedRight = min(rect.right, bitmap.width - 1)
        val fixedBottom = min(rect.bottom, bitmap.height - 1)

        // only sample pixels that surround the image
        for (xPos in fixedLeft until fixedRight step 20) {
            // sample top and bottom border
            pixels.add(bitmap[xPos, fixedTop])
            pixels.add(bitmap[xPos, fixedBottom])
        }
        for (yPos in fixedTop until fixedBottom step 20) {
            // sample left and right border
            pixels.add(bitmap[fixedLeft, yPos])
            pixels.add(bitmap[fixedRight, yPos])
        }

        // get color that appears the most often
        return pixels.groupingBy { quantize(it) }.eachCount().maxBy { it.value }.key
    }

    /**
     *  Extract the highest contrast color at a specific area in a bitmap.
     */
    fun getHighestContrastColorAtRect(bitmap: Bitmap, rect: Rect, contrastColor: Int): Int {
        val pixels = IntArray(rect.width() * rect.height())
        bitmap.getPixels(pixels, 0, rect.width(), rect.left, rect.top, rect.width(), rect.height())

        val contrastColorLuminance = ColorUtils.calculateLuminance(contrastColor)
        return pixels.maxBy {
            val l1 = ColorUtils.calculateLuminance(it)
            val l2 = contrastColorLuminance

            // https://www.accessibility-developer-guide.com/knowledge/colours-and-contrast/how-to-calculate/
            if (l1 > l2) (l1 + 0.05) / (l2 + 0.05) else (l2 + 0.05) / (l1 + 0.05)
        }
    }
}
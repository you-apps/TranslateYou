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
import android.graphics.Paint
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size


object ImageHelper {
    fun getImage(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }
    }

    fun setAlpha(originalBitmap: Bitmap, alpha: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(
            originalBitmap.getWidth(),
            originalBitmap.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(newBitmap)
        val paint = Paint().apply { this.alpha = alpha }
        canvas.drawBitmap(originalBitmap, 0f, 0f, paint)
        return newBitmap
    }

    fun cropImage(
        targetBitmap: Bitmap,
        cropSize: Size,
        cropOffset: Offset,
    ): Bitmap {
        val croppedBitmap = Bitmap.createBitmap(
            targetBitmap,
            cropOffset.x.toInt(),
            cropOffset.y.toInt(),
            cropSize.width.toInt(),
            cropSize.height.toInt()
        )

        return croppedBitmap
    }
}
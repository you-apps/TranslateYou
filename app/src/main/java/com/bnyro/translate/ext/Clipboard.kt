/*
 * Copyright (c) 2025 You Apps
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

package com.bnyro.translate.ext

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.toClipEntry

suspend fun Clipboard.setText(text: String) {
    val data = ClipData(ClipDescription(null, arrayOf("text/plain")), ClipData.Item(text))
    setClipEntry(data.toClipEntry())
}

suspend fun Clipboard.getText(): String? {
    val clipData = getClipEntry()?.clipData ?: return null
    if (clipData.itemCount == 0) return null

    return clipData.getItemAt(0).text?.toString()
}

fun Clipboard.hasText(): Boolean {
    return nativeClipboard.hasPrimaryClip()
}
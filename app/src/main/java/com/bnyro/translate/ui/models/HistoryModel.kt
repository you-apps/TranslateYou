/*
 * Copyright (c) 2023 You Apps
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

package com.bnyro.translate.ui.models

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ext.query

@SuppressLint("MutableCollectionMutableState")
class HistoryModel : ViewModel() {
    var history by mutableStateOf(listOf<HistoryItem>())

    fun fetchHistory() {
        query {
            history = history.plus(Db.historyDao().getAll().reversed())
        }
    }

    fun clearHistory() {
        query {
            Db.historyDao().deleteAll()
            history = emptyList()
        }
    }

    fun deleteHistoryItem(historyItem: HistoryItem) {
        history = history.filter { it.id != historyItem.id }
        query {
            Db.historyDao().delete(historyItem)
        }
    }
}

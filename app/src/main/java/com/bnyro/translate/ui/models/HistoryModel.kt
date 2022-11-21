package com.bnyro.translate.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ext.query

class HistoryModel : ViewModel() {
    var history by mutableStateOf(
        emptyList<HistoryItem>()
    )

    fun fetchHistory() {
        query {
            history = Db.historyDao().getAll().reversed()
        }
    }

    fun clearHistory() {
        query {
            Db.historyDao().deleteAll()
            history = listOf()
        }
    }

    fun deleteHistoryItem(historyItem: HistoryItem) {
        query {
            Db.historyDao().delete(historyItem)
        }
    }
}

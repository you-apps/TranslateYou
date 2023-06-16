package com.bnyro.translate.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ext.query

class HistoryModel : ViewModel() {
    var history = mutableStateListOf<HistoryItem>()

    fun fetchHistory() {
        query {
            history.addAll(Db.historyDao().getAll().reversed())
        }
    }

    fun clearHistory() {
        query {
            Db.historyDao().deleteAll()
            history.clear()
        }
    }

    fun deleteHistoryItem(historyItem: HistoryItem) {
        history.removeAll { it.id == historyItem.id }
        query {
            Db.historyDao().delete(historyItem)
        }
    }
}

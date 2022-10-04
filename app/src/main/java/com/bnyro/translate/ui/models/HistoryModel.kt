package com.bnyro.translate.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ext.Query
import kotlinx.coroutines.launch

class HistoryModel : ViewModel() {
    var history by mutableStateOf(
        emptyList<HistoryItem>()
    )

    fun fetchHistory() {
        viewModelScope.launch {
            Query {
                history = Db.historyDao().getAll()
            }
        }
    }
}

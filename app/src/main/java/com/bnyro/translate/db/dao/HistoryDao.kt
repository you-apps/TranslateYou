package com.bnyro.translate.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bnyro.translate.db.obj.HistoryItem

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryItem")
    fun getAll(): List<HistoryItem>

    @Insert
    fun insertAll(vararg historyItems: HistoryItem)

    @Delete
    fun delete(historyItem: HistoryItem)

    @Query("DELETE FROM HistoryItem")
    fun deleteAll()
}

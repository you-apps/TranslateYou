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
    fun insertAll(vararg HistoryItems: HistoryItem)

    @Delete
    fun delete(HistoryItem: HistoryItem)

    @Query("DELETE FROM HistoryItem")
    fun deleteAll()
}

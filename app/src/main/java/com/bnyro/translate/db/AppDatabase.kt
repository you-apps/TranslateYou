package com.bnyro.translate.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bnyro.translate.db.dao.HistoryDao
import com.bnyro.translate.db.obj.HistoryItem

@Database(
    entities = [
        HistoryItem::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

package com.bnyro.translate.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.bnyro.translate.db.dao.HistoryDao
import com.bnyro.translate.db.dao.LanguageBookmarksDao
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.db.obj.Language

@Database(
    entities = [
        HistoryItem::class,
        Language::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    abstract fun languageBookmarksDao(): LanguageBookmarksDao
}

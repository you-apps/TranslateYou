package com.bnyro.translate

import android.content.Context
import androidx.room.Room
import com.bnyro.translate.db.AppDatabase

class DatabaseHolder {
    fun initDb(applicationContext: Context) {
        Db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    companion object {
        private var DATABASE_NAME = "TYDatabase"

        lateinit var Db: AppDatabase
    }
}

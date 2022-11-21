package com.bnyro.translate.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bnyro.translate.db.obj.Language

@Dao
interface LanguageBookmarksDao {
    @Query("SELECT * FROM Language")
    fun getAll(): List<Language>

    @Insert
    fun insertAll(vararg languages: Language)

    @Delete
    fun delete(language: Language)

    @Query("DELETE FROM Language")
    fun deleteAll()
}

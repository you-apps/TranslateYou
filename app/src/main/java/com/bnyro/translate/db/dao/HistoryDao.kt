/*
 * Copyright (c) 2023 Bnyro
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

    @Query("SELECT EXISTS(SELECT * FROM HistoryItem WHERE insertedText = :insertedText AND sourceLanguageCode = :sourceLanguage AND targetLanguageCode = :targetLanguage)")
    fun existsSimilar(
        insertedText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Boolean

    @Insert
    fun insertAll(vararg historyItems: HistoryItem)

    @Delete
    fun delete(historyItem: HistoryItem)

    @Query("DELETE FROM HistoryItem")
    fun deleteAll()
}

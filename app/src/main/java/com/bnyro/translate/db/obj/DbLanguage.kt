/*
 * Copyright (c) 2023 You Apps
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

package com.bnyro.translate.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import net.youapps.translation_engines.Language

@Entity(tableName = "Language")
@Serializable
data class DbLanguage(
    @PrimaryKey(autoGenerate = false)
    val code: String = "",
    @ColumnInfo val name: String = ""
) {
    constructor(language: Language) : this(language.code, language.name)
    fun toLanguage() = Language(code, name)

    override fun equals(other: Any?): Boolean {
        (other as? DbLanguage)?.let { otherLang ->
            return otherLang.name.equals(this.name, ignoreCase = true) ||
                    otherLang.code.equals(this.code, ignoreCase = true)
        }
        return super.equals(other)
    }

    override fun hashCode() = 31 * code.hashCode() + name.hashCode()
}

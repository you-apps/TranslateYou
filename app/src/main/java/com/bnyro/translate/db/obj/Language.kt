package com.bnyro.translate.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Language(
    @PrimaryKey(autoGenerate = false)
    val code: String = "",
    @ColumnInfo val name: String = ""
)

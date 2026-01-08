/*
 * Copyright (c) 2024 You Apps
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

package net.youapps.translation_engines.mh.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MhTranslationResponse(
    @SerialName("pronunciation")
    val pronunciation: String? = null,
    @SerialName("detected")
    val detectedLanguage: String? = null,
    @SerialName("translated-text")
    val translatedText: String = "",
    @SerialName("source_synonyms") val sourceSynonyms: List<String>?,
    @SerialName("source_transliteration") val sourceTransliteration: String,
    @SerialName("target_synonyms") val targetSynonyms: List<String>?,
    @SerialName("target_transliteration") val targetTransliteration: String,
    @SerialName("word_choices") val wordChoices: List<WordChoice>? = null
)
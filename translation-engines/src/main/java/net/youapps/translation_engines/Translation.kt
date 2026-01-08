/*
 * Copyright (c) 2026 You Apps
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

package net.youapps.translation_engines

import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    val translatedText: String,
    val detectedLanguage: String? = null,
    val transliterations: List<String>? = null,
    val definitions: List<Definition>? = null,
    val similar: List<String>? = null,
    val examples: List<String>? = null
)

@Serializable
data class Definition(
    val type: String? = null,
    val definition: String? = null,
    val example: String? = null,
    val synonym: String? = null
)
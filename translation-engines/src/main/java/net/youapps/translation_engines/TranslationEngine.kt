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

enum class ApiKeyState {
    DISABLED,
    OPTIONAL,
    REQUIRED
}

interface EngineSettingsProvider {
    /**
     * Get the API url of the engine. Defaults to the [TranslationEngine.defaultUrl] of the engine if none is provided.
     *
     * This is only relevant if [TranslationEngine.urlModifiable] is `true`.
     */
    fun getApiUrl(engine: TranslationEngine): String?

    /**
     * Get the API key of the engine.
     *
     * This is only relevant if [TranslationEngine.apiKeyState] is not [ApiKeyState.DISABLED].
     */
    fun getApiKey(engine: TranslationEngine): String?

    /**
     * Get the selected model of the engine.
     * Must be one of [TranslationEngine.supportedModels].
     *
     * This is only relevant if [TranslationEngine.supportedModels] is not empty.
     */
    fun getSelectedModel(engine: TranslationEngine): String?
}

abstract class TranslationEngine(private val settingsProvider: EngineSettingsProvider) {
    /**
     * The canonical name of the translation engine.
     */
    abstract val name: String

    /**
     * Default HTTPS base url for the engine.
     */
    abstract val defaultUrl: String

    /**
     * Set to true, if the engine is self-hostable and thus different instance URLs are possible.
     */
    abstract val urlModifiable: Boolean

    /**
     * Whether an API key is required or supported.
     */
    abstract val apiKeyState: ApiKeyState

    /**
     * Set to null to disable automatic language detection for this engine.
     */
    abstract val autoLanguageCode: String?

    /**
     * If set to true, [getAudioFile] has to be implemented as well.
     */
    open val supportsAudio: Boolean = false

    /**
     * Supported translation models by this engine.
     */
    open val supportedModels: List<String> = listOf()

    /**
     * Initialize the translation engine using the API url provided by [settingsProvider] using [EngineSettingsProvider.getApiUrl].
     *
     * This must be called once before using the engine for the first time and whenever the API url has changed.
     */
    abstract fun createOrRecreate(): TranslationEngine

    /**
     * Get the list of languages the translation engines supports.
     *
     * This method might crash if there are errors during the API request or when parsing the response.
     */
    abstract suspend fun getLanguages(): List<Language>

    /**
     * Translate the [query] text from the given two-letter [source] language code to the given two-letter [target] language code.
     *
     * This method might crash if there are errors during the API request or when parsing the response.
     */
    abstract suspend fun translate(query: String, source: String, target: String): Translation

    /**
     * Run Text To Speech on a given [query] text.
     *
     * This only works if [supportsAudio] is `true`.
     *
     * @param lang: a two-letter language code
     * @param query: the text to be pronounced by the text to speech API
     * @return raw bytes of an audio file (e.g. mp3). Usually, these bytes must be stored to a file
     * and afterwards can be directly passed to a media player.
     */
    open suspend fun getAudioFile(lang: String, query: String): ByteArray? = null

    // Helper methods using the [settingsProvider]
    fun getUrl() = settingsProvider.getApiUrl(this) ?: defaultUrl
    fun getApiKey() = settingsProvider.getApiKey(this)
    fun getSelectedModel() = settingsProvider.getSelectedModel(this) ?: supportedModels.firstOrNull()

    protected fun sourceOrAuto(source: String): String {
        return source.ifEmpty { autoLanguageCode }.orEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (other is TranslationEngine) {
            return this.name == other.name && this.getUrl() == other.getUrl() && this.getApiKey() == other.getApiKey()
        }

        return false
    }
}
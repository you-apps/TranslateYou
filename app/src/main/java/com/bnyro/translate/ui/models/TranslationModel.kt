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

package com.bnyro.translate.ui.models

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.R
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.db.obj.HistoryItemType
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TessHelper
import com.bnyro.translate.util.TranslationEngine
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import androidx.compose.runtime.mutableStateMapOf
import com.bnyro.translate.ext.toastFromMainThread

class TranslationModel : ViewModel() {
    var engine by mutableStateOf(getCurrentEngine())

    var simTranslationEnabled by mutableStateOf(
        Preferences.get(Preferences.simultaneousTranslationKey, false)
    )
    var enabledSimEngines = getEnabledEngines()

    var availableLanguages: List<Language> by mutableStateOf(emptyList())

    var sourceLanguage by mutableStateOf(
        getLanguageByPrefKey(Preferences.sourceLanguage) ?: Language("", "Auto")
    )

    var targetLanguage by mutableStateOf(
        getLanguageByPrefKey(Preferences.targetLanguage) ?: Language("en", "English")
    )

    var insertedText by mutableStateOf("")

    var translation by mutableStateOf(Translation(""))

    var translatedTexts = mutableStateMapOf<String, Translation>()
    
    var inverseTranslatedTexts = mutableStateMapOf<String, Translation>()

    var bookmarkedLanguages by mutableStateOf(listOf<Language>())

    var translating by mutableStateOf(false)
    private val _apiError = MutableStateFlow<Exception?>(null)
    val apiError = _apiError.asStateFlow()

    private var simTranslationJobs = emptyList<Job>()

    private var mediaPlayer: MediaPlayer? = null
    private var audioFile: File? = null

    fun detectedLanguageMostly(): String?{
        val detectedLanguageList = translatedTexts.filter {
            it.value.detectedLanguage != null
        }
        return detectedLanguageList.maxByOrNull {
            it.value.detectedLanguage?:""
        }?.value?.detectedLanguage
      
    fun mostDetectedLanguage(): Language? {
        if (!simTranslationEnabled) return availableLanguages.find { it.code == translation.detectedLanguage }

        val mostDetectedLangCode = translatedTexts.values
            .filter { !it.detectedLanguage.isNullOrEmpty() }
            .ifEmpty { return null }
            .groupBy { it.detectedLanguage }
            .maxBy { it.value.size }
            .key

        return availableLanguages.find { it.code == mostDetectedLangCode }
    }

    private fun getLanguageByPrefKey(key: String): Language? {
        return runCatching {
            JsonHelper.json.decodeFromString<Language>(Preferences.get(key, ""))
        }.getOrNull()
    }

    fun enqueueTranslation() {
        if (!Preferences.get(Preferences.translateAutomatically, true)) return

        val insertedTextTemp = insertedText
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == insertedText) translateNow()
            },
            Preferences.get(
                Preferences.fetchDelay,
                800f
            ).toLong()
        )
    }

    fun translateNow() {
        if (insertedText.isEmpty() || targetLanguage == sourceLanguage) {
            translation = Translation("")
            return
        }
        saveSelectedLanguages()

        translating = true

        translatedTexts.clear()
        TranslationEngines.engines.forEach { translatedTexts[it.name] = Translation("") }

        // reset translations
        translatedTexts = TranslationEngines.engines
            .associate { it.name to Translation("") }

        viewModelScope.launch(Dispatchers.IO) {
            val translation = try {
                engine.translate(
                    insertedText,
                    sourceLanguage.code,
                    targetLanguage.code
                )
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                _apiError.emit(e)
                translating = false
                Translation("")
            }
            translating = false

            if (insertedText.isNotEmpty()) {
                this@TranslationModel.translation = translation
                 translatedTexts = translatedTexts.toMutableMap().also {
                     it[engine.name] = translation
                 }.toMap()
                saveToHistory()
            }
        }

        if (simTranslationEnabled) simTranslation()
    }

    private fun simTranslation() = viewModelScope.launch(Dispatchers.IO) {
        // cancel old jobs from previous translation
        simTranslationJobs.forEach { if (it.isActive) it.cancel() }

        simTranslationJobs = enabledSimEngines
            .filter { it != engine }
            .map {
                async {
                    runCatching {
                        val translation = it.translate(
                            insertedText,
                            sourceLanguage.code,
                            targetLanguage.code
                        )

                        //if sourceLanguage "auto" use detectedLanguage
                        val sourceLanguageCode = if(sourceLanguage.code == ""){
                            val detectedLanguage = translatedTexts[it.name]?.detectedLanguage
                            if (detectedLanguage == "" || detectedLanguage == null){
                                detectedLanguageMostly()
                            }else{ detectedLanguage }?:""
                        } else { sourceLanguage.code }

                        inverseTranslatedTexts[it.name] = it.translate(
                            translatedTexts[it.name]?.translatedText?:return@runCatching,
                            targetLanguage.code,
                            sourceLanguageCode
                        )
                        
                        translatedTexts = translatedTexts.toMutableMap().also { translations ->
                            translations[it.name] = translation
                        }.toMap()
                    }
                }
            }
    }

    private fun saveToHistory() {
        if (!Preferences.get(Preferences.historyEnabledKey, true)) {
            return
        }

        save(HistoryItemType.HISTORY)
    }

    fun saveToFavorites() = save(HistoryItemType.FAVORITE)

    private fun save(itemType: HistoryItemType) {
        val historyItem = HistoryItem(
            sourceLanguageCode = sourceLanguage.code,
            sourceLanguageName = sourceLanguage.name,
            targetLanguageCode = targetLanguage.code,
            targetLanguageName = targetLanguage.name,
            insertedText = insertedText,
            translatedText = translation.translatedText,
            itemType = itemType
        )

        viewModelScope.launch(Dispatchers.IO) {
            // don't create new entry if a similar one exists
            if (Preferences.get(Preferences.skipSimilarHistoryKey, true) && Db.historyDao()
                    .existsSimilar(
                        historyItem.insertedText,
                        historyItem.sourceLanguageCode,
                        historyItem.targetLanguageCode,
                        itemType = itemType
                    )
            ) return@launch

            Db.historyDao().insertAll(historyItem)
        }
    }

    fun clearTranslation() {
        insertedText = ""
        translation = Translation("")
        translating = false
    }

    private fun fetchLanguages() {
        viewModelScope.launch {
            val languages = try {
                Log.e("engine", engine.name)
                engine.getLanguages().sortedBy { it.name }
            } catch (e: Exception) {
                Log.e("Fetching languages", e.toString())
                _apiError.emit(e)
                return@launch
            }
            this@TranslationModel.availableLanguages = languages
            sourceLanguage = replaceLanguageName(sourceLanguage)
            targetLanguage = replaceLanguageName(targetLanguage)
        }
    }

    private fun replaceLanguageName(language: Language): Language {
        return availableLanguages.firstOrNull { it.code == language.code } ?: language
    }

    private fun getCurrentEngine() = TranslationEngines.engines.find {
        it.name == Preferences.get(Preferences.selectedEngineKey, TranslationEngines.engines.first().name)
    } ?: TranslationEngines.engines.first()

    fun setCurrentEngine(engine: TranslationEngine) {
        Preferences.put(Preferences.selectedEngineKey, engine.name)
        this.engine = engine
    }

    private fun getEnabledEngines() = TranslationEngines.engines.filter {
        it.isSimultaneousTranslationEnabled()
    }

    fun refresh() {
        val newSelectedEngine = getCurrentEngine()
        if (newSelectedEngine != engine) {
            engine = newSelectedEngine
            enqueueTranslation()
        }

        enabledSimEngines = getEnabledEngines()
        simTranslationEnabled = Preferences.get(Preferences.simultaneousTranslationKey, false)

        fetchLanguages()

        fetchBookmarkedLanguages()
    }

    private fun fetchBookmarkedLanguages() = viewModelScope.launch(Dispatchers.IO) {
        bookmarkedLanguages = Db.languageBookmarksDao().getAll()
    }

    fun processImage(context: Context, image: Bitmap) {
        if (!TessHelper.areLanguagesDownloaded(context)) {
            Toast.makeText(context, R.string.init_tess_first, Toast.LENGTH_SHORT).show()
            return
        }
        Thread {
            TessHelper.getText(context, image)?.let {
                insertedText = it
                translateNow()
            }
        }.start()
    }

    fun saveSelectedLanguages() {
        Preferences.put(
            Preferences.sourceLanguage,
            JsonHelper.json.encodeToString(sourceLanguage)
        )
        Preferences.put(
            Preferences.targetLanguage,
            JsonHelper.json.encodeToString(targetLanguage)
        )
    }

    fun swapLanguages() {
        if (availableLanguages.isEmpty()) return

        val temp = if (sourceLanguage.code.isEmpty()) {
            mostDetectedLanguage() ?: return
        } else {
            sourceLanguage
        }
        sourceLanguage = targetLanguage
        targetLanguage = temp

        if (translation.translatedText.isNotEmpty()) {
            insertedText = translation.translatedText
            translation = Translation("")
        }

        translateNow()
    }

    fun playAudio(languageCode: String, text: String) {
        releaseMediaPlayer()

        viewModelScope.launch {
            audioFile = runCatching {
                withContext(Dispatchers.IO) {
                    engine.getAudioFile(languageCode, text)
                }
            }.getOrNull()
            val audioFile = audioFile ?: return@launch

            mediaPlayer = MediaPlayer().apply {
                setOnCompletionListener {
                    releaseMediaPlayer()
                }

                setDataSource(audioFile.absolutePath)

                runCatching {
                    prepare()
                    start()
                }
            }
        }
    }

    private fun releaseMediaPlayer() {
        audioFile?.delete()
        audioFile = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

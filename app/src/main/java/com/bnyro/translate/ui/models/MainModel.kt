package com.bnyro.translate.ui.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ext.Query
import com.bnyro.translate.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TranslationEngine
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainModel : ViewModel() {
    var engine: TranslationEngine = getCurrentEngine()

    var simTranslationEnabled by mutableStateOf(
        Preferences.get(Preferences.simultaneousTranslationKey, false)
    )
    var enabledSimEngines = getEnabledEngines()

    var availableLanguages: List<Language> by mutableStateOf(
        emptyList()
    )

    var sourceLanguage: Language by mutableStateOf(
        getLanguageByPrefKey(Preferences.sourceLanguage) ?: Language("auto", "Auto")
    )

    var targetLanguage: Language by mutableStateOf(
        getLanguageByPrefKey(Preferences.targetLanguage) ?: Language("en", "English")
    )

    var insertedText: String by mutableStateOf(
        ""
    )

    var translation: Translation by mutableStateOf(
        Translation("")
    )

    var translatedTexts: MutableMap<String, Translation> =
        TranslationEngines.engines
            .associate { it.name to Translation("") }
            .toMutableMap()

    private fun getLanguageByPrefKey(key: String): Language? {
        return try {
            ObjectMapper().readValue(
                Preferences.get(key, ""),
                Language::class.java
            )
        } catch (e: Exception) {
            null
        }
    }

    fun enqueueTranslation() {
        val insertedTextTemp = insertedText
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == insertedText) translate()
            },
            Preferences.get(
                Preferences.fetchDelay,
                500f
            ).toLong()
        )
    }

    fun translate() {
        if (insertedText == "" || targetLanguage == sourceLanguage) {
            translation = Translation("")
            return
        }

        translatedTexts = TranslationEngines.engines
            .associate { it.name to Translation("") }
            .toMutableMap()

        CoroutineScope(Dispatchers.IO).launch {
            /*
            val translation = try {
                engine.translate(
                    insertedText,
                    sourceLanguage.code,
                    targetLanguage.code
                )
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@launch
            }
             */
            val translation = engine.translate(
                insertedText,
                sourceLanguage.code,
                targetLanguage.code
            )
            if (insertedText != "") {
                this@MainModel.translation = translation
                translatedTexts[engine.name] = translation
                saveToHistory()
            }
        }

        if (simTranslationEnabled) simTranslation()
    }

    private fun simTranslation() {
        enabledSimEngines.forEach {
            if (it != engine) {
                CoroutineScope(Dispatchers.IO).launch {
                    val translation = try {
                        it.translate(
                            insertedText,
                            sourceLanguage.code,
                            targetLanguage.code
                        )
                    } catch (e: Exception) {
                        return@launch
                    }
                    translatedTexts[it.name] = translation
                }
            }
        }
    }

    private fun saveToHistory() {
        if (!Preferences.get(
                Preferences.historyEnabledKey,
                true
            )
        ) {
            return
        }
        Query {
            Db.historyDao().insertAll(
                HistoryItem(
                    sourceLanguageCode = sourceLanguage.code,
                    sourceLanguageName = sourceLanguage.name,
                    targetLanguageCode = targetLanguage.code,
                    targetLanguageName = targetLanguage.name,
                    insertedText = insertedText,
                    translatedText = translation.translatedText
                )
            )
        }
    }

    fun clearTranslation() {
        insertedText = ""
        translation = Translation("")
    }

    fun fetchLanguages(onError: (Exception) -> Unit = {}) {
        viewModelScope.launch {
            val languages = try {
                engine.getLanguages()
            } catch (e: Exception) {
                Log.e("Fetching languages", e.toString())
                onError.invoke(e)
                return@launch
            }
            this@MainModel.availableLanguages = languages
        }
    }

    private fun getCurrentEngine() = TranslationEngines.engines[
        Preferences.get(Preferences.apiTypeKey, 0)
    ]

    private fun getEnabledEngines() = TranslationEngines.engines.filter {
        it.isSimultaneousTranslationEnabled()
    }

    fun refresh() {
        engine = getCurrentEngine()
        enabledSimEngines = getEnabledEngines()
        simTranslationEnabled = Preferences.get(Preferences.simultaneousTranslationKey, false)
    }
}

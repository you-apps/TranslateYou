package com.bnyro.translate.ui.models

import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.api.ApiHelper
import com.bnyro.translate.api.lt.LTHelper
import com.bnyro.translate.api.lv.LVHelper
import com.bnyro.translate.constants.ApiType
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.Preferences
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.launch

class MainModel : ViewModel() {
    private val apiHelper: ApiHelper = when (
        Preferences.get(
            Preferences.apiTypeKey,
            ApiType.LIBRE_TRANSLATE
        )
    ) {
        ApiType.LINGVA_TRANSLATE -> LTHelper()
        else -> LVHelper()
    }

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

    var translatedText: String by mutableStateOf(
        ""
    )

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
        android.os.Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == insertedText) translate()
            },
            700
        )
    }

    private fun translate() {
        if (insertedText == "" || targetLanguage == sourceLanguage) {
            translatedText = ""
            return
        }

        viewModelScope.launch {
            val translation = try {
                apiHelper.translate(
                    insertedText,
                    sourceLanguage.code!!,
                    targetLanguage.code!!
                )
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@launch
            }
            if (insertedText != "") translatedText = translation
        }
    }

    fun clearTranslation() {
        insertedText = ""
        translatedText = ""
    }

    fun fetchLanguages() {
        viewModelScope.launch {
            val languages = try {
                apiHelper.getLanguages()
            } catch (e: Exception) {
                Log.e("error", e.toString())
                return@launch
            }
            this@MainModel.availableLanguages = languages
        }
    }
}

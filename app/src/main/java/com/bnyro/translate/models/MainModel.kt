package com.bnyro.translate.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitInstance
import kotlinx.coroutines.launch

class MainModel : ViewModel() {
    var availableLanguages: List<Language> by mutableStateOf(
        emptyList()
    )

    var sourceLanguage: String by mutableStateOf(
        ""
    )

    var targetLanguage: String by mutableStateOf(
        "en"
    )

    val text: String by mutableStateOf(
        "en"
    )

    var translation: String by mutableStateOf(
        ""
    )

    fun translate() {
        viewModelScope.launch {
            val translation = try {
                RetrofitInstance.api.translate(
                    text,
                    sourceLanguage,
                    targetLanguage
                )
            } catch (e: Exception) {
                return@launch
            }
            this@MainModel.translation = translation.translatedText ?: ""
            Log.e("translation", translation.translatedText!!)
        }
    }

    fun fetchLanguages() {
        viewModelScope.launch {
            val languages = try {
                RetrofitInstance.api.getLanguages()
            } catch (e: Exception) {
                return@launch
            }
            this@MainModel.availableLanguages = languages
        }
    }
}

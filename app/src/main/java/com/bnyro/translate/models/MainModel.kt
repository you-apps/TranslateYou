package com.bnyro.translate.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.util.RetrofitInstance
import kotlinx.coroutines.launch

class MainModel: ViewModel() {
    val translation = MutableLiveData<String>().apply {
        value = ""
    }

    fun translate() {
        viewModelScope.launch {
            val translation = RetrofitInstance.api.translate(
                "Hallo", "de", "en"
            )
            Log.e("translation", translation.translatedText!!)
        }
    }
}

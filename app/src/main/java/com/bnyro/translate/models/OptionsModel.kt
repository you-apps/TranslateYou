package com.bnyro.translate.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OptionsModel : ViewModel() {
    var openDialog by mutableStateOf(false)
}

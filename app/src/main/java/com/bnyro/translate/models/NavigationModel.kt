package com.bnyro.translate.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NavigationModel : ViewModel() {
    var showOptions by mutableStateOf(false)
    var showAbout by mutableStateOf(false)
}

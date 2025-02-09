package dev.benedek.rig

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainScreenViewModel: ViewModel() {
    var alphaState by mutableStateOf(true)
        private set

    fun toggleAlphaSwitch(isOn: Boolean) {
        alphaState = isOn
    }
}
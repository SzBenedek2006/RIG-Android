package dev.benedek.rig

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel






class MainScreenViewModel: ViewModel() {
    var alphaState by mutableStateOf(false)
        private set
    var format by mutableStateOf("PNG")
        private set
    var presses by mutableIntStateOf(0)
        private set
    var progressPercent by mutableStateOf<Float?>(null)
        private set

    var finished by mutableStateOf(false)
        private set
    var doRender by mutableStateOf(false)
        private set
    var alpha by mutableStateOf(false)
        private set
    var quality by mutableIntStateOf(100)
        private set
    var width by mutableIntStateOf(0)
        private set
    var height by mutableIntStateOf(0)
        private set
    var count by mutableIntStateOf(1)
        private set
    var currentCount by mutableIntStateOf(1)
        private set
    var stop by mutableStateOf(false)
        private set


    fun toggleAlphaSwitch(newState: Boolean) {
        alphaState = newState
    }
    fun setFormatTo(newFormat: String) {
        format = newFormat
    }
    fun incrementPresses() {
        presses++
    }
    
    fun updateProgressPercent(newPercent: Float?) {
        progressPercent = newPercent
    }

    fun updateFinished(newState: Boolean) {
        finished = newState
    }
    fun updateDoRender(newState: Boolean) {
        doRender = newState
    }
    fun updateAlpha(newState: Boolean) {
        alpha = newState
    }
    fun updateQuality(newQuality: Int) {
        quality = newQuality
    }
    fun updateWidth(newWidth: Int) {
        width = newWidth
    }
    fun updateHeight(newHeight: Int) {
        height = newHeight
    }
    fun updateCount(newCount: Int) {
        count = newCount
    }
    fun updateCurrentCount(newCurrentCount: Int) {
        currentCount = newCurrentCount
    }
    fun updateStop(newState: Boolean) {
        stop = newState
    }


}
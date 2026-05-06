package com.example.themeswitcher.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themeswitcher.data.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    val themeMode: StateFlow<Int> = settingsManager.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun setThemeMode(mode: Int) {
        viewModelScope.launch {
            settingsManager.saveThemeMode(mode)
        }
    }
}
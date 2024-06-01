package com.example.networkspeedo.domain.settings

import com.example.networkspeedo.data.PreferenceStorage
import com.example.networkspeedo.domain.ThemeState
import javax.inject.Inject

class GetThemeStateUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(): ThemeState {
        return when(storage.getTheme()) {
            PreferenceStorage.PREF_VALUE_SYSTEM_THEME -> ThemeState.System
            PreferenceStorage.PREF_VALUE_DARK_THEME -> ThemeState.Dark
            PreferenceStorage.PREF_VALUE_LIGHT_THEME -> ThemeState.Light
            else -> ThemeState.System
        }
    }
}
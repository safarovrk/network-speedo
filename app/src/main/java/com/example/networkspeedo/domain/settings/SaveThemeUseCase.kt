package com.example.networkspeedo.domain.settings

import com.example.networkspeedo.data.PreferenceStorage
import com.example.networkspeedo.domain.ThemeState
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(theme: ThemeState) {
        when(theme) {
            ThemeState.System -> storage.saveTheme(PreferenceStorage.PREF_VALUE_SYSTEM_THEME)
            ThemeState.Dark -> storage.saveTheme(PreferenceStorage.PREF_VALUE_DARK_THEME)
            ThemeState.Light -> storage.saveTheme(PreferenceStorage.PREF_VALUE_LIGHT_THEME)
        }
    }
}
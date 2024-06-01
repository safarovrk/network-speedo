package com.example.networkspeedo.presentation

import androidx.lifecycle.ViewModel
import com.example.networkspeedo.domain.ThemeState
import com.example.networkspeedo.domain.settings.GetThemeStateUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getThemeStateUseCase: GetThemeStateUseCase
) : ViewModel() {
    suspend fun getTheme(): ThemeState = getThemeStateUseCase.execute()
}
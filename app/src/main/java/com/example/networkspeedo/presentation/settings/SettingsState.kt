package com.example.networkspeedo.presentation.settings

import com.example.networkspeedo.domain.ThemeState

class SettingsState (
    val themeState: ThemeState,
    val loadEndpoint: String,
    val uploadEndpoint: String,
    val loadCheckboxState: Boolean,
    val uploadCheckboxState: Boolean
)
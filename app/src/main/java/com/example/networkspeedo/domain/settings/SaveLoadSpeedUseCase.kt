package com.example.networkspeedo.domain.settings

import com.example.networkspeedo.data.PreferenceStorage
import javax.inject.Inject

class SaveLoadSpeedUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(value: Boolean) {
        storage.saveLoadCheckboxState(value)
    }
}
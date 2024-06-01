package com.example.networkspeedo.domain

import com.example.networkspeedo.data.PreferenceStorage
import javax.inject.Inject

class GetLoadSpeedStateUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(): Boolean {
        return storage.getLoadCheckboxState()
    }
}
package com.example.networkspeedo.domain.settings

import com.example.networkspeedo.data.PreferenceStorage
import javax.inject.Inject

class SaveLoadEndpointUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(endpoint: String) {
        storage.saveLoadEndpoint(endpoint)
    }
}
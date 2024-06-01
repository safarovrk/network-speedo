package com.example.networkspeedo.domain.settings

import com.example.networkspeedo.data.PreferenceStorage
import javax.inject.Inject

class SaveUploadEndpointUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(endpoint: String) {
        storage.saveUploadEndpoint(endpoint)
    }
}
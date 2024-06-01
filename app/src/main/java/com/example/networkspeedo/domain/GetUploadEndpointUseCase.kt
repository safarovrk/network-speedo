package com.example.networkspeedo.domain

import com.example.networkspeedo.data.PreferenceStorage
import javax.inject.Inject

class GetUploadEndpointUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(): String {
        return storage.getUploadEndpoint()
    }
}
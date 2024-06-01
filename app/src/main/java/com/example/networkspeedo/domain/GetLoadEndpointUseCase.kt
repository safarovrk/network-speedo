package com.example.networkspeedo.domain

import com.example.networkspeedo.data.PreferenceStorage
import javax.inject.Inject

class GetLoadEndpointUseCase @Inject constructor(
    private val storage: PreferenceStorage
) {
    suspend fun execute(): String {
        return storage.getLoadEndpoint()
    }
}
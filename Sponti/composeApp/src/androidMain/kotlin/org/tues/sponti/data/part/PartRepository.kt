package org.tues.sponti.data.part

import org.tues.sponti.data.chal.ChallengeDto
import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response

class PartRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getActiveParticipations(): Response<List<ChallengeDto>> {
        return api.getActiveParticipations()
    }

    suspend fun getAllCompletedCount(): Response<Int> {
        return api.getAllCompletedCount()
    }
}
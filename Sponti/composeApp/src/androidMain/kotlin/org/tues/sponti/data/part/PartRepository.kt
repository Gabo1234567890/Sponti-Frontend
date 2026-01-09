package org.tues.sponti.data.part

import org.tues.sponti.data.chal.ChallengeDto
import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.GetParticipationStatusResponse
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response

class PartRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getActiveParticipations(): Response<List<ChallengeDto>> {
        return api.getActiveParticipations()
    }

    suspend fun getAllCompletedCount(): Response<Int> {
        return api.getAllCompletedCount()
    }

    suspend fun getParticipationStatus(challengeId: String): Response<GetParticipationStatusResponse> {
        return api.getParticipationStatus(challengeId = challengeId)
    }

    suspend fun getChallengeCompletedCount(challengeId: String): Response<Int> {
        return api.getChallengeCompletedCount(challengeId = challengeId)
    }
}
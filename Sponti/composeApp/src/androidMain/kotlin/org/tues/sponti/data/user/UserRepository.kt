package org.tues.sponti.data.user

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.GetCurrentUserDataResponse
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response

class UserRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getCurrentUser(
        memoryPage: Int = 1,
        memoryPerPage: Int = 10
    ): Response<GetCurrentUserDataResponse> {
        return api.getCurrentUser(memoryPage = memoryPage, memoryPerPage = memoryPerPage)
    }
}
package org.tues.sponti.data.user

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.GetMemoriesResponse
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response

class UserRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getCurrentUser(): Response<UserDto> {
        return api.getCurrentUser()
    }

    suspend fun getMemories(
        page: Int = 1,
        perPage: Int = 10
    ): Response<GetMemoriesResponse> {
        return api.getMemories(
            page = page,
            perPage = perPage
        )
    }
}
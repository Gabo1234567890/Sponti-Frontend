package org.tues.sponti.data.user

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.GetAccountDetailsResponse
import org.tues.sponti.data.network.GetCurrentUserDataResponse
import org.tues.sponti.data.network.PatchCurrentUserRequest
import org.tues.sponti.data.network.PatchCurrentUserResponse
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response

class UserRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getCurrentUser(
        memoryPage: Int = 1,
        memoryPerPage: Int = 10
    ): Response<GetCurrentUserDataResponse> {
        return api.getCurrentUser(memoryPage = memoryPage, memoryPerPage = memoryPerPage)
    }

    suspend fun getAccountDetails(): Response<GetAccountDetailsResponse> {
        return api.getAccountDetails()
    }

    suspend fun patchCurrentUser(username: String? = null, allowPublicImages: Boolean? = null): Response<PatchCurrentUserResponse> {
        val req = PatchCurrentUserRequest(username, allowPublicImages)
        return api.patchCurrentUser(req)
    }

    suspend fun deleteCurrentUser(): Response<String> {
        return api.deleteCurrentUser()
    }
}
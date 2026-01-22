package org.tues.sponti.data.admin

import org.tues.sponti.data.chal.ChallengeDto
import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.GetAdminChallengesResponse
import org.tues.sponti.data.network.GetAllUsersResponse
import org.tues.sponti.data.network.MessageResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.network.UpdateUserRoleRequest
import org.tues.sponti.data.network.UpdateUserRoleResponse
import org.tues.sponti.data.user.Role
import retrofit2.Response

class AdminRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getAllUsers(page: Int = 1, perPage: Int = 10): Response<GetAllUsersResponse> {
        return api.getAllUsers(page = page, perPage = perPage)
    }


    suspend fun updateUserRole(userId: String, role: Role): Response<UpdateUserRoleResponse> {
        return api.updateUserRole(id = userId, body = UpdateUserRoleRequest(role))
    }


    suspend fun deleteUser(userId: String): Response<MessageResponse> {
        return api.deleteUser(id = userId)
    }

    suspend fun getChallenges(
        approved: Boolean,
        page: Int = 1,
        perPage: Int = 20
    ): Response<GetAdminChallengesResponse> {
        return api.getChallenges(approved = approved, page = page, perPage = perPage)
    }


    suspend fun approveChallenge(challengeId: String): Response<ChallengeDto> {
        return api.approveChallenge(id = challengeId)
    }


    suspend fun deleteChallenge(challengeId: String): Response<MessageResponse> {
        return api.deleteChallenge(id = challengeId)
    }
}
package org.tues.sponti.data.auth

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.network.SignupRequest
import org.tues.sponti.data.network.SignupResponse
import retrofit2.Response

class AuthRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun signup(username: String, email: String, password: String): Response<SignupResponse> {
        val req = SignupRequest(username, email, password)
        return api.signup(req)
    }
}
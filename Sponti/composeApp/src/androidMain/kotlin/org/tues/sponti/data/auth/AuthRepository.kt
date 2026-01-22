package org.tues.sponti.data.auth

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.LoginRequest
import org.tues.sponti.data.network.LoginResponse
import org.tues.sponti.data.network.MessageResponse
import org.tues.sponti.data.network.RefreshTokensRequest
import org.tues.sponti.data.network.RefreshTokensResponse
import org.tues.sponti.data.network.RequestPasswordResetRequest
import org.tues.sponti.data.network.ResetPasswordRequest
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.network.SignupRequest
import org.tues.sponti.data.network.SignupResponse
import retrofit2.Response

class AuthRepository(private val api: ApiService = RetrofitClient.api): AuthApi {
    suspend fun signup(
        username: String,
        email: String,
        password: String
    ): Response<SignupResponse> {
        val req = SignupRequest(username, email, password)
        return api.signup(req)
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val req = LoginRequest(email, password)
        return api.login(req)
    }

    suspend fun requestPasswordReset(email: String): Response<MessageResponse> {
        val req = RequestPasswordResetRequest(email)
        return api.requestPasswordReset(req)
    }

    suspend fun resetPassword(
        token: String,
        email: String,
        password: String
    ): Response<MessageResponse> {
        val req = ResetPasswordRequest(password)
        return api.resetPassword(token, email, req)
    }

    suspend fun verifyEmail(token: String, email: String): Response<MessageResponse> {
        return api.verifyEmail(token, email)
    }

    override suspend fun refreshTokens(refreshToken: String): Response<RefreshTokensResponse> {
        val req = RefreshTokensRequest(refreshToken)
        return api.refreshTokens(req)
    }

    suspend fun logout(): Response<MessageResponse> {
        return api.logout()
    }
}
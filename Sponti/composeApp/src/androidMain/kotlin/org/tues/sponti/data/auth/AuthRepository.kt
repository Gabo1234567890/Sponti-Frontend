package org.tues.sponti.data.auth

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.LoginRequest
import org.tues.sponti.data.network.LoginResponse
import org.tues.sponti.data.network.RequestPasswordResetRequest
import org.tues.sponti.data.network.RequestPasswordResetResponse
import org.tues.sponti.data.network.ResetPasswordRequest
import org.tues.sponti.data.network.ResetPasswordResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.network.SignupRequest
import org.tues.sponti.data.network.SignupResponse
import org.tues.sponti.data.network.VerifyEmailResponse
import retrofit2.Response

class AuthRepository(private val api: ApiService = RetrofitClient.api) {
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

    suspend fun requestPasswordReset(email: String): Response<RequestPasswordResetResponse> {
        val req = RequestPasswordResetRequest(email)
        return api.requestPasswordReset(req)
    }

    suspend fun resetPassword(
        token: String,
        email: String,
        password: String
    ): Response<ResetPasswordResponse> {
        val req = ResetPasswordRequest(password)
        return api.resetPassword(token, email, req)
    }

    suspend fun verifyEmail(token: String, email: String): Response<VerifyEmailResponse> {
        return api.verifyEmail(token, email)
    }
}
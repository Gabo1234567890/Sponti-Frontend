package org.tues.sponti.data.auth

import org.tues.sponti.data.network.RefreshTokensResponse
import retrofit2.Response

interface AuthApi {
    suspend fun refreshTokens(refreshToken: String): Response<RefreshTokensResponse>
}
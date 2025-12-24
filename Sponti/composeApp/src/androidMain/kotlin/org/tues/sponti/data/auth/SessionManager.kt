package org.tues.sponti.data.auth

class SessionManager(private val authManager: AuthManager, private val authRepository: AuthRepository) {
    fun getAccessToken(): String? {
        return authManager.getAccessToken()
    }

    suspend fun refreshIfNeeded(): Boolean {
        val refreshToken = authManager.getRefreshToken() ?: return false

        val response = authRepository.refreshTokens(refreshToken)

        if(!response.isSuccessful) {
            authManager.clearTokens()
            return false
        }

        val body = response.body() ?: return false
        if(body.accessToken.isNullOrEmpty() || body.refreshToken.isNullOrEmpty()) return false

        authManager.saveTokens(
            body.accessToken,
            body.refreshToken
        )

        return true
    }

    fun login(accessToken: String, refreshToken: String) {
        authManager.saveTokens(accessToken, refreshToken)
    }

    fun logout() {
        authManager.clearTokens()
    }
}
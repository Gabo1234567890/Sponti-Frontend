package org.tues.sponti.data.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SessionManager(private val authManager: AuthManager, private val authApi: AuthApi) {
    private val refreshMutex = Mutex()
    private var ongoingRefresh: Boolean = false

    fun getAccessToken(): String? {
        return authManager.getAccessToken()
    }

    suspend fun refreshIfNeeded(): Boolean {
        refreshMutex.withLock {
            if (ongoingRefresh) {
                return authManager.getAccessToken() != null
            }

            ongoingRefresh = true

            try {
                val refreshToken = authManager.getRefreshToken() ?: return false

                val response = authApi.refreshTokens(refreshToken)

                if (!response.isSuccessful) {
                    authManager.clearTokens()
                    return false
                }

                val body = response.body() ?: return false
                if (body.accessToken.isNullOrEmpty() || body.refreshToken.isNullOrEmpty()) return false

                authManager.saveTokens(
                    body.accessToken,
                    body.refreshToken
                )

                return true
            } finally {
                ongoingRefresh = false
            }
        }
    }

    fun login(accessToken: String, refreshToken: String) {
        authManager.saveTokens(accessToken, refreshToken)
    }

    fun logout() {
        authManager.clearTokens()
    }
}
package org.tues.sponti.data.auth

import android.content.Context

class AuthTokenStoreImplementation(context: Context): AuthTokenStore {
    private val authManager = AuthManager(context)

    override fun save(accessToken: String, refreshToken: String) {
        authManager.saveTokens(accessToken, refreshToken)
    }

    override fun clear() {
        authManager.clearTokens()
    }
}
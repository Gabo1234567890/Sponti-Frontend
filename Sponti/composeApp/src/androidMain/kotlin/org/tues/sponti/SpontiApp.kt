package org.tues.sponti

import android.app.Application
import org.tues.sponti.data.auth.AuthApi
import org.tues.sponti.data.auth.AuthManager
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.data.auth.SessionManager
import org.tues.sponti.data.network.RetrofitClient

class SpontiApp: Application() {
    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()

        val authManager = AuthManager(applicationContext)

        lateinit var authApi: AuthApi

        sessionManager = SessionManager(
            authManager = authManager,
            authApi = object : AuthApi {
                override suspend fun refreshTokens(refreshToken: String) =
                    authApi.refreshTokens(refreshToken)
            }
        )

        RetrofitClient.init(sessionManager)

        authApi = AuthRepository()
    }
}
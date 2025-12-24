package org.tues.sponti.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.tues.sponti.data.auth.SessionManager

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val accessToken = runBlocking {
            sessionManager.getAccessToken()
        }

        val requestWithToken = if (accessToken != null) {
            original.newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
        } else {
            original
        }

        val response = chain.proceed(requestWithToken)

        if (response.code != 401) {
            return response
        }

        response.close()

        val refreshed = runBlocking {
            sessionManager.refreshIfNeeded()
        }

        if (!refreshed) return response

        val newToken = runBlocking {
            sessionManager.getAccessToken()
        } ?: return response

        val retryRequest =
            original.newBuilder().addHeader("Authorization", "Bearer $newToken").build()

        return chain.proceed(retryRequest)
    }

}
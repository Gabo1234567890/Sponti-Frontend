package org.tues.sponti.data.auth

import android.content.Context
import androidx.core.content.edit

class AuthManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
        }
    }

    fun clearTokens() {
        prefs.edit { clear() }
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }
}
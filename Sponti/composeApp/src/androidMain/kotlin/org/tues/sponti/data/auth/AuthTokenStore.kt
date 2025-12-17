package org.tues.sponti.data.auth

interface AuthTokenStore {
    fun save(accessToken: String, refreshToken: String)
    fun clear()
}
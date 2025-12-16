package org.tues.sponti.data.network

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class ErrorResponse(val message: Any?, val error: String?, val statusCode: Int?){
    fun getMessage(): String? {
        return when(message) {
            is String -> message
            is List<*> -> message.firstOrNull()?.toString()
            else -> null
        }
    }
}

data class SignupRequest(val username: String, val email: String, val password: String)
data class SignupResponse(val id: String?, val email: String?, val statusCode: Int?)

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val accessToken: String?, val refreshToken: String?)

interface ApiService {
    @POST("/auth/signup")
    suspend fun signup(@Body req: SignupRequest): Response<SignupResponse>

    @POST("/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>
}

package org.tues.sponti.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class SignupRequest(val username: String, val email: String, val password: String)
data class SignupResponse(val id: String?, val message: String?)

interface ApiService {
    @POST("/auth/signup")
    suspend fun signup(@Body req: SignupRequest): Response<SignupResponse>
}

package org.tues.sponti.data.network

import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tues.sponti.data.chal.ChallengeDto
import org.tues.sponti.data.part.CompletionImageDto
import org.tues.sponti.data.part.ParticipationDto
import org.tues.sponti.data.part.PublicCompletionImage
import org.tues.sponti.data.user.MemoryItemDto
import org.tues.sponti.data.user.UserDto
import org.tues.sponti.ui.screens.common.FieldError
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class ErrorResponse(val message: Any?, val error: String?, val statusCode: Int?) {
    fun getMessage(): String? {
        return when (message) {
            is String -> message
            is List<*> -> message.firstOrNull()?.toString()
            else -> null
        }
    }

    fun toFieldError(): FieldError {
        return (getMessage() ?: error)?.let { FieldError.Server(it) } ?: FieldError.Unknown
    }
}

data class SignupRequest(val username: String, val email: String, val password: String)
data class SignupResponse(val id: String?, val email: String?, val statusCode: Int?)

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val accessToken: String?, val refreshToken: String?)

data class RequestPasswordResetRequest(val email: String)

data class RequestPasswordResetResponse(val message: String?)

data class ResetPasswordRequest(val password: String)

data class ResetPasswordResponse(val message: String?)

data class VerifyEmailResponse(val message: String?)

data class RefreshTokensRequest(val refreshToken: String)

data class RefreshTokensResponse(val accessToken: String?, val refreshToken: String?)

@JsonClass(generateAdapter = true)
data class FetchChallengesByFiltersResponse(
    val items: List<ChallengeDto>?, val count: Int?, val page: Int?, val perPage: Int?
)

@JsonClass(generateAdapter = true)
data class GetMemoriesResponse(
    val items: List<MemoryItemDto>?, val page: Int?, val perPage: Int?
)

@JsonClass(generateAdapter = true)
data class GetParticipationStatusResponse(
    val exists: Boolean?, val isActive: Boolean?, val completionCount: Int?, val startedAt: String?
)

@JsonClass(generateAdapter = true)
data class GetPublicCompletionImagesResponse(
    val items: List<PublicCompletionImage>?,
    val page: Int?,
    val perPage: Int?
)

interface ApiService {
    @POST("/auth/signup")
    suspend fun signup(@Body req: SignupRequest): Response<SignupResponse>

    @POST("/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @POST("/auth/request-password-reset")
    suspend fun requestPasswordReset(@Body req: RequestPasswordResetRequest): Response<RequestPasswordResetResponse>

    @POST("/auth/reset-password")
    suspend fun resetPassword(
        @Query("token") token: String,
        @Query("email") email: String,
        @Body req: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    @GET("/auth/verify-email")
    suspend fun verifyEmail(
        @Query("token") token: String, @Query("email") email: String
    ): Response<VerifyEmailResponse>

    @POST("/auth/refresh")
    suspend fun refreshTokens(@Body req: RefreshTokensRequest): Response<RefreshTokensResponse>

    @GET("/challenges")
    suspend fun fetchChallengesByFilters(
        @Query("minPrice") minPrice: Int?,
        @Query("maxPrice") maxPrice: Int?,
        @Query("minDuration") minDuration: Int?,
        @Query("maxDuration") maxDuration: Int?,
        @Query("vehicles") vehicles: List<String>?,
        @Query("placeTypes") placeTypes: List<String>?,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<FetchChallengesByFiltersResponse>

    @Multipart
    @POST("/challenges/submit")
    suspend fun submitChallenge(
        @Part thumbnail: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("durationMinutes") durationMinutes: RequestBody,
        @Part("place") place: RequestBody,
        @Part("vehicle") vehicle: RequestBody,
        @Part("placeType") placeType: RequestBody
    ): Response<ChallengeDto>

    @GET("challenges/{id}")
    suspend fun getChallengeById(@Path("id") id: String): Response<ChallengeDto>

    @GET("/user/me")
    suspend fun getCurrentUser(): Response<UserDto>

    @GET("/user/memories")
    suspend fun getMemories(
        @Query("page") page: Int, @Query("perPage") perPage: Int
    ): Response<GetMemoriesResponse>

    @POST("participations/{challengeId}/start")
    suspend fun startChallenge(@Path("challengeId") challengeId: String): Response<ParticipationDto>

    @PATCH("participations/{challengeId}/cancel")
    suspend fun cancelChallenge(@Path("challengeId") challengeId: String): Response<ParticipationDto>

    @PATCH("participations/{challengeId}/complete")
    suspend fun completeChallenge(@Path("challengeId") challengeId: String): Response<ParticipationDto>

    @GET("/participations/active")
    suspend fun getActiveParticipations(): Response<List<ChallengeDto>>

    @GET("participations/completed-count")
    suspend fun getAllCompletedCount(): Response<Int>

    @GET("participations/{challengeId}/status")
    suspend fun getParticipationStatus(@Path("challengeId") challengeId: String): Response<GetParticipationStatusResponse>

    @GET("participations/{challengeId}/completed-count")
    suspend fun getChallengeCompletedCount(@Path("challengeId") challengeId: String): Response<Int>

    @GET("participations/{challengeId}/public-images")
    suspend fun getPublicCompletionImages(
        @Path("challengeId") challengeId: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<GetPublicCompletionImagesResponse>
}

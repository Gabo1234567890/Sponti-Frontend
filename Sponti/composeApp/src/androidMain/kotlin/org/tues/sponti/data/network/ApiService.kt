package org.tues.sponti.data.network

import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tues.sponti.data.chal.ChallengeDto
import org.tues.sponti.data.chal.VehicleType
import org.tues.sponti.data.part.CompletionImageDto
import org.tues.sponti.data.part.ParticipationDto
import org.tues.sponti.data.part.PublicCompletionImage
import org.tues.sponti.data.user.MemoryItemDto
import org.tues.sponti.data.user.Role
import org.tues.sponti.data.user.UserDto
import org.tues.sponti.ui.screens.common.FieldError
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

data class ResetPasswordRequest(val password: String)

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
data class GetCurrentUserDataResponse(
    val user: UserDto?,
    val memories: GetMemoriesResponse?,
    val activeChallenges: List<ChallengeDto>?,
    val completedCount: Int?
)

@JsonClass(generateAdapter = true)
data class GetParticipationStatusResponse(
    val exists: Boolean?, val isActive: Boolean?, val completionCount: Int?, val startedAt: String?
)

@JsonClass(generateAdapter = true)
data class GetPublicCompletionImagesResponse(
    val items: List<PublicCompletionImage>?, val page: Int?, val perPage: Int?
)

@JsonClass(generateAdapter = true)
data class GetChallengeByIdResponse(
    val challenge: ChallengeDto?,
    val publicCompletionImages: GetPublicCompletionImagesResponse?,
    val status: GetParticipationStatusResponse?
)

@JsonClass(generateAdapter = true)
data class GetAccountDetailsResponse(
    val username: String?, val email: String?, val allowPublicImages: Boolean?, val role: Role?
)

@JsonClass(generateAdapter = true)
data class PatchCurrentUserRequest(
    val username: String?, val allowPublicImages: Boolean?
)

@JsonClass(generateAdapter = true)
data class PatchCurrentUserResponse(
    val id: String?, val email: String?, val username: String?, val allowPublicImages: Boolean?
)

data class AdminUserListItem(
    val id: String?,
    val username: String?,
    val email: String?,
    val allowPublicImages: Boolean?,
    val role: Role?,
    val emailVerified: Boolean?,
    val createdAt: String?
)

@JsonClass(generateAdapter = true)
data class GetAllUsersResponse(
    val items: List<AdminUserListItem>?,
    val total: Int?,
    val page: Int?,
    val perPage: Int?,
    val totalPages: Int?
)

@JsonClass(generateAdapter = true)
data class UpdateUserRoleRequest(
    val role: Role
)

@JsonClass(generateAdapter = true)
data class UpdateUserRoleResponse(
    val id: String?, val role: Role?
)

data class AdminChallengeListItem(
    val id: String?,
    val title: String?,
    val description: String?,
    val thumbnailUrl: String?,
    val price: Int?,
    val durationMinutes: Int?,
    val place: String?,
    val vehicle: VehicleType?,
    val submittedByUserId: String?,
    val createdAt: String?
)

@JsonClass(generateAdapter = true)
data class GetAdminChallengesResponse(
    val items: List<AdminChallengeListItem>?,
    val total: Int?,
    val page: Int?,
    val perPage: Int?,
    val totalPages: Int?
)

@JsonClass(generateAdapter = true)
data class MessageResponse(val message: String?)

interface ApiService {
    @POST("/auth/signup")
    suspend fun signup(@Body req: SignupRequest): Response<SignupResponse>

    @POST("/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @POST("/auth/request-password-reset")
    suspend fun requestPasswordReset(@Body req: RequestPasswordResetRequest): Response<MessageResponse>

    @POST("/auth/reset-password")
    suspend fun resetPassword(
        @Query("token") token: String,
        @Query("email") email: String,
        @Body req: ResetPasswordRequest
    ): Response<MessageResponse>

    @GET("/auth/verify-email")
    suspend fun verifyEmail(
        @Query("token") token: String, @Query("email") email: String
    ): Response<MessageResponse>

    @POST("/auth/refresh")
    suspend fun refreshTokens(@Body req: RefreshTokensRequest): Response<RefreshTokensResponse>

    @POST("/auth/logout")
    suspend fun logout(): Response<MessageResponse>

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

    @GET("/challenges/{id}")
    suspend fun getChallengeById(
        @Path("id") id: String,
        @Query("completionImagesPage") completionImagesPage: Int,
        @Query("completionImagesPerPage") completionImagesPerPage: Int
    ): Response<GetChallengeByIdResponse>

    @GET("/user/me")
    suspend fun getCurrentUser(
        @Query("memoryPage") memoryPage: Int, @Query("memoryPerPage") memoryPerPage: Int
    ): Response<GetCurrentUserDataResponse>

    @GET("/user/account-details")
    suspend fun getAccountDetails(): Response<GetAccountDetailsResponse>

    @PATCH("/user/me")
    suspend fun patchCurrentUser(@Body req: PatchCurrentUserRequest): Response<PatchCurrentUserResponse>

    @DELETE("/user/me")
    suspend fun deleteCurrentUser(): Response<MessageResponse>

    @Multipart
    @POST("/participations/{challengeId}/images")
    suspend fun uploadImages(
        @Path("challengeId") challengeId: String, @Part images: List<MultipartBody.Part>
    ): Response<List<CompletionImageDto>>

    @POST("/participations/{challengeId}/start")
    suspend fun startChallenge(@Path("challengeId") challengeId: String): Response<ParticipationDto>

    @PATCH("/participations/{challengeId}/cancel")
    suspend fun cancelChallenge(@Path("challengeId") challengeId: String): Response<ParticipationDto>

    @PATCH("/participations/{challengeId}/complete")
    suspend fun completeChallenge(@Path("challengeId") challengeId: String): Response<ParticipationDto>

    @GET("/admin/users")
    suspend fun getAllUsers(
        @Query("page") page: Int, @Query("perPage") perPage: Int
    ): Response<GetAllUsersResponse>

    @PATCH("/admin/users/{id}/role")
    suspend fun updateUserRole(
        @Path("id") id: String, @Body body: UpdateUserRoleRequest
    ): Response<UpdateUserRoleResponse>

    @DELETE("/admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<MessageResponse>

    @GET("/admin/challenges")
    suspend fun getChallenges(
        @Query("approved") approved: Boolean,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<GetAdminChallengesResponse>

    @PATCH("/admin/challenges/{id}/approve")
    suspend fun approveChallenge(@Path("id") id: String): Response<ChallengeDto>

    @DELETE("/admin/challenges/{id}")
    suspend fun deleteChallenge(@Path("id") id: String): Response<MessageResponse>
}

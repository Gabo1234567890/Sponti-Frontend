package org.tues.sponti.data.part

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tues.sponti.data.chal.ChallengeDto
import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.GetParticipationStatusResponse
import org.tues.sponti.data.network.GetPublicCompletionImagesResponse
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response
import java.io.File

class PartRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun startChallenge(challengeId: String): Response<ParticipationDto> {
        return api.startChallenge(challengeId = challengeId)
    }

    suspend fun cancelChallenge(challengeId: String): Response<ParticipationDto> {
        return api.cancelChallenge(challengeId = challengeId)
    }

    suspend fun completeChallenge(challengeId: String): Response<ParticipationDto> {
        return api.completeChallenge(challengeId = challengeId)
    }

    suspend fun getActiveParticipations(): Response<List<ChallengeDto>> {
        return api.getActiveParticipations()
    }

    suspend fun getAllCompletedCount(): Response<Int> {
        return api.getAllCompletedCount()
    }

    suspend fun getParticipationStatus(challengeId: String): Response<GetParticipationStatusResponse> {
        return api.getParticipationStatus(challengeId = challengeId)
    }

    suspend fun getChallengeCompletedCount(challengeId: String): Response<Int> {
        return api.getChallengeCompletedCount(challengeId = challengeId)
    }

    suspend fun getPublicCompletionImages(
        challengeId: String,
        page: Int = 1,
        perPage: Int = 10
    ): Response<GetPublicCompletionImagesResponse> {
        return api.getPublicCompletionImages(
            challengeId = challengeId,
            page = page,
            perPage = perPage
        )
    }

    suspend fun uploadImages(
        challengeId: String,
        images: List<File>
    ): Response<List<CompletionImageDto>> {

        val imageParts = images.map { file ->
            MultipartBody.Part.createFormData(
                name = "images",
                filename = file.name,
                body = file.asRequestBody("image/*".toMediaType())
            )
        }

        return api.uploadImages(
            challengeId = challengeId,
            images = imageParts
        )
    }
}
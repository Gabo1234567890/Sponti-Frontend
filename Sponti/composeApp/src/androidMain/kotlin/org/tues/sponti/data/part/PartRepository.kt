package org.tues.sponti.data.part

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tues.sponti.data.network.ApiService
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
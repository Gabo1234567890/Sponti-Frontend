package org.tues.sponti.data.chal

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.FetchChallengesByFiltersResponse
import org.tues.sponti.data.network.GetChallengeByIdResponse
import org.tues.sponti.data.network.RetrofitClient
import retrofit2.Response
import java.io.File

class ChalRepository(private val api: ApiService = RetrofitClient.api) {
    private val _challengesInvalidated = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val challengesInvalidated = _challengesInvalidated.asSharedFlow()

    suspend fun invalidateChallenges() {
        _challengesInvalidated.emit(Unit)
    }

    suspend fun fetchChallengesByFilters(
        minPrice: Int?,
        maxPrice: Int?,
        minDuration: Int?,
        maxDuration: Int?,
        vehicles: List<VehicleType>?,
        placeTypes: List<PlaceType>?,
        page: Int = 1,
        perPage: Int = 20
    ): Response<FetchChallengesByFiltersResponse> {
        return api.fetchChallengesByFilters(
            minPrice = minPrice,
            maxPrice = maxPrice,
            minDuration = minDuration,
            maxDuration = maxDuration,
            vehicles = vehicles?.map { it.name.lowercase() },
            placeTypes = placeTypes?.map { it.name.lowercase() },
            page = page,
            perPage = perPage
        )
    }

    suspend fun submitChallenge(
        thumbnail: File?,
        title: String,
        description: String,
        price: Int,
        durationMinutes: Int,
        place: String,
        vehicle: VehicleType,
        placeType: PlaceType
    ): Response<ChallengeDto> {

        val imagePart = if (thumbnail != null) MultipartBody.Part.createFormData(
            name = "thumbnail",
            filename = thumbnail.name,
            body = thumbnail.asRequestBody("image/*".toMediaType())
        ) else null

        return api.submitChallenge(
            thumbnail = imagePart,
            title = title.toRequestBody(),
            description = description.toRequestBody(),
            price = price.toString().toRequestBody(),
            durationMinutes = durationMinutes.toString().toRequestBody(),
            place = place.toRequestBody(),
            vehicle = vehicle.name.lowercase().toRequestBody(),
            placeType = placeType.name.lowercase().toRequestBody()
        )
    }

    suspend fun getChallengeById(
        id: String,
        completionImagesPage: Int = 1,
        completionImagesPerPage: Int = 10
    ): Response<GetChallengeByIdResponse> {
        return api.getChallengeById(
            id = id,
            completionImagesPage = completionImagesPage,
            completionImagesPerPage = completionImagesPerPage
        )
    }
}
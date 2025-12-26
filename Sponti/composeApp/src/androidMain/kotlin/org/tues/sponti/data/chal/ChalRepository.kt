package org.tues.sponti.data.chal

import org.tues.sponti.data.network.ApiService
import org.tues.sponti.data.network.FetchChallengesByFiltersResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.ui.screens.common.PlaceType
import org.tues.sponti.ui.screens.common.VehicleType
import retrofit2.Response

class ChalRepository(private val api: ApiService = RetrofitClient.api) {
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
}
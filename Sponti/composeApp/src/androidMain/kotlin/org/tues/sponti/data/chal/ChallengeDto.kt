package org.tues.sponti.data.chal

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChallengeDto(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val price: Int,
    val durationMinutes: Int,
    val place: String,
    val vehicle: VehicleType,
    val placeType: PlaceType,
    val approved: Boolean,
    val submittedByUserId: String?,
    val createdAt: String,
    val updatedAt: String
)
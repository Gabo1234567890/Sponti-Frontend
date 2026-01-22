package org.tues.sponti.ui.screens.common

import com.squareup.moshi.JsonClass
import org.tues.sponti.data.chal.PlaceType
import org.tues.sponti.data.chal.VehicleType

@JsonClass(generateAdapter = true)
data class ChallengeType(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val price: Int,
    val duration: Int,
    val place: String,
    val vehicle: VehicleType,
    val placeType: PlaceType,
    val approved: Boolean
)
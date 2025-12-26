package org.tues.sponti.ui.screens.common

data class ChallengeType(
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val price: Int,
    val duration: Int,
    val place: String,
    val vehicle: VehicleType,
    val placeType: PlaceType,
)
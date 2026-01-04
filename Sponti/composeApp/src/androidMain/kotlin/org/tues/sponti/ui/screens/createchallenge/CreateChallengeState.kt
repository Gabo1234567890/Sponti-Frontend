package org.tues.sponti.ui.screens.createchallenge

import org.tues.sponti.data.chal.PlaceType
import org.tues.sponti.data.chal.VehicleType
import org.tues.sponti.ui.screens.common.FieldError
import java.io.File

data class CreateChallengeState(
    val title: String = "",
    val description: String = "",
    val thumbnail: File? = null,
    val price: String = "",
    val duration: String = "",
    val vehicle: VehicleType = VehicleType.CAR,
    val place: String = "",
    val placeType: PlaceType = PlaceType.ANYWHERE,

    val globalError: FieldError? = null,

    val isSubmitting: Boolean = false
)
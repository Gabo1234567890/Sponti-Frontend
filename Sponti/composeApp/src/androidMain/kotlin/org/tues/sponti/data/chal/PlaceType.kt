package org.tues.sponti.data.chal

import com.squareup.moshi.Json

enum class PlaceType {
    @Json(name = "indoor")
    INDOOR,
    @Json(name = "outdoor")
    OUTDOOR,
    @Json(name = "anywhere")
    ANYWHERE
}
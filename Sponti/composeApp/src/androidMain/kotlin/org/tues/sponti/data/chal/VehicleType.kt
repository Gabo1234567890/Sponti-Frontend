package org.tues.sponti.data.chal

import com.squareup.moshi.Json

enum class VehicleType {
    @Json(name = "car")
    CAR,
    @Json(name = "walking")
    WALKING,
    @Json(name = "plane")
    PLANE,
    @Json(name = "train")
    TRAIN,
    @Json(name = "bicycle")
    BICYCLE
}
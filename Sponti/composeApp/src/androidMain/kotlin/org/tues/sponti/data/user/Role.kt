package org.tues.sponti.data.user

import com.squareup.moshi.Json

enum class Role {
    @Json(name = "user")
    USER,
    @Json(name = "admin")
    ADMIN
}
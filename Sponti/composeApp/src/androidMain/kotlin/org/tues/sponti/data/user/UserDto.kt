package org.tues.sponti.data.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val emailVerified: Boolean,
    val allowPublicImages: Boolean,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)
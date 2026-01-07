package org.tues.sponti.data.part

data class ParticipationDto(
    val userId: String,
    val challengeId: String,
    val isActive: Boolean,
    val startedAt: String?,
    val completionCount: Int,
    val createdAt: String,
    val updatedAt: String
)
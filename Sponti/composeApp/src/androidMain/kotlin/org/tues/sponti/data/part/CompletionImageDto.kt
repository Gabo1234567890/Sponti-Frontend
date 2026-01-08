package org.tues.sponti.data.part

data class CompletionImageDto(
    val id: String,
    val userId: String,
    val challengeId: String,
    val url: String,
    val uploadedAt: String
)
package org.tues.sponti.ui.screens.common

import org.tues.sponti.data.chal.ChallengeDto

fun String.containsAllCharacterTypes(): Boolean {
    val hasLowercase = Regex("[a-z]").containsMatchIn(this)
    val hasUppercase = Regex("[A-Z]").containsMatchIn(this)
    val hasDigit = Regex("\\d").containsMatchIn(this)
    val hasSpecialChar = Regex("[^A-Za-z0-9]").containsMatchIn(this)

    return hasLowercase && hasUppercase && hasDigit && hasSpecialChar
}

fun Int.minutesToFormattedTimeString(): String {
    val hours = this / 60
    val minutes = this % 60

    val formattedHours = if (hours < 10) "0$hours" else hours.toString()
    val formattedMinutes = if (minutes < 10) "0$minutes" else minutes.toString()

    return "$formattedHours:$formattedMinutes"
}

fun String.formattedTimeToMinutes(): Int {
    val time = this.split(':')

    val hours = time[0].toInt() * 60
    val minutes = time[1].toInt()

    return hours + minutes
}

fun ChallengeDto.toUi(): ChallengeType {
    return ChallengeType(
        id = id,
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        price = price,
        duration = durationMinutes,
        place = place,
        vehicle = vehicle,
        placeType = placeType
    )
}
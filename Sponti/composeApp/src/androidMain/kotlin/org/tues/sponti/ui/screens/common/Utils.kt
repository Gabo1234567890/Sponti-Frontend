package org.tues.sponti.ui.screens.common

fun String.containsAllCharacterTypes(): Boolean {
    val hasLowercase = Regex("[a-z]").containsMatchIn(this)
    val hasUppercase = Regex("[A-Z]").containsMatchIn(this)
    val hasDigit = Regex("\\d").containsMatchIn(this)
    val hasSpecialChar = Regex("[^A-Za-z0-9]").containsMatchIn(this)

    return hasLowercase && hasUppercase && hasDigit && hasSpecialChar
}

fun Int.minutesToFormattedTimeString(): String {
    val hours = this / 60

    return if (hours < 10) {
        "0$hours:${this % 60}"
    } else "$hours:${this % 60}"
}
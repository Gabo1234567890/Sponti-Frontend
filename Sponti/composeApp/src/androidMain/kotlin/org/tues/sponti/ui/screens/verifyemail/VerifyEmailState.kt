package org.tues.sponti.ui.screens.verifyemail

data class VerifyEmailState(
    val step: VerifyEmailStep = VerifyEmailStep.CHECK_INBOX
)
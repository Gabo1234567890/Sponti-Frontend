package org.tues.sponti.ui.screens.forgotpassword

import org.tues.sponti.ui.screens.common.FieldError

data class ForgotPasswordState(
    val step: Int = 0,
    val email: String = "",
    val password: String = "",
    val newPassword: String = "",
    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
    val newPasswordError: FieldError? = null,
    val globalError: FieldError? = null,
    val isLoading: Boolean = false
)
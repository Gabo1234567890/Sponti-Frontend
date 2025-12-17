package org.tues.sponti.ui.screens.login

import org.tues.sponti.ui.screens.common.FieldError

data class LoginState(
    val email: String = "",
    val password: String = "",

    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
    val globalError: FieldError? = null,

    val isSubmitting: Boolean = false
)
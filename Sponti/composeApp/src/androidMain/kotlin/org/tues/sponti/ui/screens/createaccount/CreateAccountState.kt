package org.tues.sponti.ui.screens.createaccount

import org.tues.sponti.ui.screens.common.FieldError

data class CreateAccountState(
    val username: String = "",
    val email: String = "",
    val password: String = "",

    val usernameError: FieldError? = null,
    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
    val globalError: FieldError? = null,

    val isSubmitting: Boolean = false
)
package org.tues.sponti.ui.screens.settings

import org.tues.sponti.data.user.Role
import org.tues.sponti.ui.screens.common.FieldError

data class SettingsState(
    val originalUsername: String = "",
    val username: String = "",
    val usernameError: FieldError? = null,
    val email: String = "",
    val viewAccountDetails: Boolean = false,
    val allowPublicImages: Boolean = false,
    val role: Role = Role.USER,
    val globalError: FieldError? = null,
    val isLoading: Boolean = false
)
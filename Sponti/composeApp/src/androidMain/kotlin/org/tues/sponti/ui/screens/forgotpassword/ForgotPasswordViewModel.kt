package org.tues.sponti.ui.screens.forgotpassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.containsAllCharacterTypes

class ForgotPasswordViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val initialStep: ForgotPasswordStep = if (!savedStateHandle.get<String>("token")
            .isNullOrEmpty()
    ) ForgotPasswordStep.RESET_PASSWORD
    else ForgotPasswordStep.ENTER_EMAIL

    private val _state = MutableStateFlow(ForgotPasswordState(step = initialStep))
    val state = _state.asStateFlow()

    fun onStepChange(value: ForgotPasswordStep) {
        _state.value = _state.value.copy(
            step = value
        )
    }

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(
            email = value, emailError = null
        )
    }

    fun sendEmail() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()) {
            _state.value = _state.value.copy(emailError = FieldError.InvalidFormat)
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = authRepository.requestPasswordReset(_state.value.email.trim())
                if (resp.isSuccessful) {
                    _state.update { it.copy(step = ForgotPasswordStep.CHECK_INBOX) }
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(emailError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(
            password = value, passwordError = null
        )
    }

    fun onNewPasswordChange(value: String) {
        _state.value = _state.value.copy(
            newPassword = value, newPasswordError = null
        )
    }

    fun resetPassword(token: String, email: String) {
        val s = _state.value

        when {
            s.password.length < 8 -> {
                _state.value = s.copy(passwordError = FieldError.Short)
                return
            }

            !s.password.containsAllCharacterTypes() -> {
                _state.value = s.copy(passwordError = FieldError.Weak)
                return
            }

            s.newPassword.length < 8 -> {
                _state.value = s.copy(newPasswordError = FieldError.Short)
                return
            }

            !s.newPassword.containsAllCharacterTypes() -> {
                _state.value = s.copy(newPasswordError = FieldError.Weak)
                return
            }

            s.password != s.newPassword -> {
                _state.value = s.copy(
                    passwordError = FieldError.NoMatch, newPasswordError = FieldError.NoMatch
                )
                return
            }
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp =
                    authRepository.resetPassword(token.trim(), email.trim(), s.password.trim())
                if (resp.isSuccessful) {
                    _state.update { it.copy(step = ForgotPasswordStep.SUCCESS) }
                } else {
                    _state.update { it.copy(step = ForgotPasswordStep.FAILURE) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }

    }
}
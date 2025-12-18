package org.tues.sponti.ui.screens.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.containsAllCharacterTypes

class ForgotPasswordViewModel(private val authRepository: AuthRepository = AuthRepository()) :
    ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(
            email = value, emailError = null
        )
    }

    fun sendEmail(onSuccess: () -> Unit) {
        val s = _state.value

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) {
            _state.value = s.copy(emailError = FieldError.InvalidFormat)
            return
        }

        viewModelScope.launch {
            _state.value = s.copy(isLoading = true)
            try {
                val resp = authRepository.requestPasswordReset(s.email.trim())
                if (resp.isSuccessful) {
                    onSuccess()
                    _state.value = s.copy(
                        step = 1
                    )
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.value = s.copy(
                        emailError = parsed?.toFieldError()
                    )
                }
            } catch (_: Exception) {
                _state.value = s.copy(
                    globalError = FieldError.Network
                )
            } finally {
                _state.value = s.copy(
                    isLoading = false
                )
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

    fun resetPassword(token: String, onSuccess: () -> Unit, onFail: () -> Unit) {
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
        }
        viewModelScope.launch {
            _state.value = s.copy(isLoading = true)
            try {
                val resp =
                    authRepository.resetPassword(token.trim(), s.email.trim(), s.password.trim())
                if(resp.isSuccessful) {
                    onSuccess()
                }
                else {
                    onFail()
                }
            } catch (_: Exception) {
                onFail()
            } finally {
                _state.value = s.copy(
                    step = 3,
                    isLoading = false
                )
            }
        }

    }
}
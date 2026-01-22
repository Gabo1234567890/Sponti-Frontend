package org.tues.sponti.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.data.auth.SessionManager
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.containsAllCharacterTypes

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(
            email = value, emailError = null
        )
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(
            password = value, passwordError = null
        )
    }

    fun clearGlobalError() {
        _state.update { it.copy(globalError = null) }
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value

        when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches() -> {
                _state.value = s.copy(emailError = FieldError.InvalidFormat)
                return
            }

            s.password.length < 8 -> {
                _state.value = s.copy(passwordError = FieldError.Short)
                return
            }

            !s.password.containsAllCharacterTypes() -> {
                _state.value = s.copy(passwordError = FieldError.Weak)
                return
            }
        }

        viewModelScope.launch {
            _state.value = s.copy(isSubmitting = true)

            try {
                val resp = authRepository.login(s.email.trim(), s.password.trim())

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.accessToken != null && body.refreshToken != null) {
                        sessionManager.login(
                            accessToken = body.accessToken,
                            refreshToken = body.refreshToken
                        )
                        onSuccess()
                    } else {
                        _state.value = _state.value.copy(
                            globalError = FieldError.InvalidFormat
                        )
                    }
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }

                    when {
                        parsed?.getMessage()?.lowercase()
                            ?.contains("email") == true || parsed?.error?.lowercase()
                            ?.contains("email") == true -> _state.value = _state.value.copy(
                            emailError = parsed.toFieldError()
                        )

                        parsed?.getMessage()?.lowercase()
                            ?.contains("password") == true || parsed?.error?.lowercase()
                            ?.contains("password") == true -> _state.value = _state.value.copy(
                            passwordError = parsed.toFieldError()
                        )

                        parsed?.getMessage()?.lowercase()
                            ?.contains("invalid credentials") == true || parsed?.error?.lowercase()
                            ?.contains("invalid credentials") == true -> _state.value =
                            _state.value.copy(
                                emailError = parsed.toFieldError(),
                                passwordError = parsed.toFieldError()
                            )

                        else -> _state.value = _state.value.copy(
                            globalError = parsed?.toFieldError()
                        )
                    }
                }
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    globalError = FieldError.Network
                )
            } finally {
                _state.value = _state.value.copy(
                    isSubmitting = false
                )
            }
        }
    }
}
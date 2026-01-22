package org.tues.sponti.ui.screens.settings

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
import org.tues.sponti.data.user.UserRepository
import org.tues.sponti.ui.screens.common.FieldError

class SettingsViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository(),
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        loadAccountDetails()
    }

    fun onUsernameChange(value: String) {
        _state.update { it.copy(username = value, usernameError = null) }
    }

    fun onViewAccountDetailsChange(value: Boolean) {
        _state.update { it.copy(viewAccountDetails = value) }
    }

    fun clearGlobalError() {
        _state.update { it.copy(globalError = null) }
    }

    fun loadAccountDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = userRepository.getAccountDetails()

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.username != null && body.email != null && body.allowPublicImages != null && body.role != null) {
                        _state.update {
                            it.copy(
                                originalUsername = body.username,
                                username = body.username,
                                email = body.email,
                                allowPublicImages = body.allowPublicImages,
                                role = body.role
                            )
                        }
                    }
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }

                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun patchUserData(
        username: String? = null,
        allowPublicImages: Boolean? = null
    ) {
        if (username?.isBlank() == true) {
            _state.update { it.copy(usernameError = FieldError.Empty) }
            return
        }

        if (username == _state.value.originalUsername) {
            _state.update { it.copy(viewAccountDetails = false) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = userRepository.patchCurrentUser(
                    username = username?.trim(),
                    allowPublicImages = allowPublicImages
                )

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.username != null && body.allowPublicImages != null) {
                        _state.update {
                            it.copy(
                                originalUsername = body.username,
                                username = body.username,
                                allowPublicImages = body.allowPublicImages,
                                viewAccountDetails = false
                            )
                        }
                    }
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }

                    if (
                        parsed?.getMessage()?.lowercase()
                            ?.contains("username") == true || parsed?.error?.lowercase()
                            ?.contains("username") == true
                    ) {
                        _state.update { it.copy(usernameError = parsed.toFieldError()) }
                    } else {
                        _state.update { it.copy(globalError = parsed?.toFieldError()) }
                    }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                sessionManager.logout()
                onLogout()
            }
        }
    }

    fun deleteAccount(onDelete: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.deleteCurrentUser()
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                sessionManager.logout()
                onDelete()
            }
        }
    }
}
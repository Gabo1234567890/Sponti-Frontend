package org.tues.sponti.ui.screens.verifyemail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.data.auth.AuthRepository

class VerifyEmailViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val token: String? = savedStateHandle["token"]
    private val email: String? = savedStateHandle["email"]

    private val _state = MutableStateFlow(
        VerifyEmailState(
            step = if (token.isNullOrEmpty() || email.isNullOrEmpty()) VerifyEmailStep.CHECK_INBOX
            else VerifyEmailStep.VERIFYING
        )
    )
    val state = _state.asStateFlow()

    init {
        if(!token.isNullOrEmpty() && !email.isNullOrEmpty()) verifyEmail(token, email)
    }

    fun verifyEmail(token: String, email: String) {
        viewModelScope.launch {
            try {
                val resp = authRepository.verifyEmail(token.trim(), email.trim())
                if (resp.isSuccessful) {
                    _state.update { it.copy(step = VerifyEmailStep.SUCCESS) }
                } else {
                    _state.update { it.copy(step = VerifyEmailStep.FAILURE) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(step = VerifyEmailStep.FAILURE) }
            }
        }
    }
}
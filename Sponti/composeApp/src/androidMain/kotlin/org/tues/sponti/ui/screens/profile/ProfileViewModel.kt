package org.tues.sponti.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.core.AppEvents
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.user.UserRepository
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.toUi

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        getUserData()

        viewModelScope.launch {
            AppEvents.challengeInteracted.collect {
                getUserData()
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = userRepository.getCurrentUser()

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.user != null && body.memories?.items != null && body.activeChallenges != null && body.completedCount != null) {
                        _state.update {
                            it.copy(
                                userData = body.user,
                                memories = body.memories.items,
                                activeChallenge = body.activeChallenges.map { dto -> dto.toUi() },
                                completedCount = body.completedCount
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

    fun clearGlobalError() {
        _state.update { it.copy(globalError = null) }
    }
}
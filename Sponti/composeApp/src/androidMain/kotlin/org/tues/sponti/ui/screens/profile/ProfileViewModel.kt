package org.tues.sponti.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.part.PartRepository
import org.tues.sponti.data.user.UserRepository
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.toUi

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val partRepository: PartRepository = PartRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        getUserData()
        getActiveChallenges()
        getCompletedCount()
        getMemories()
    }

    fun getUserData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = userRepository.getCurrentUser()

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body != null) {
                        _state.update { it.copy(userData = body) }
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

    fun getActiveChallenges() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.getActiveParticipations()

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body != null) {
                        _state.update { it.copy(activeChallenge = body.map { dto -> dto.toUi() }) }
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

    fun getCompletedCount() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.getAllCompletedCount()

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body != null) {
                        _state.update { it.copy(completedCount = body) }
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

    fun getMemories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = userRepository.getMemories()

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.items != null) {
                        _state.update { it.copy(memories = body.items) }
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
}
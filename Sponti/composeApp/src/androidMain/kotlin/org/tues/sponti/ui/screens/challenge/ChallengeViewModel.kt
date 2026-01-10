package org.tues.sponti.ui.screens.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.data.chal.ChalRepository
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.part.PartRepository
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.toUi

class ChallengeViewModel(
    private val chalRepository: ChalRepository = ChalRepository(),
    private val partRepository: PartRepository = PartRepository(),
    challengeId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ChallengeState())
    val state = _state.asStateFlow()

    init {
        getChallengeData(challengeId = challengeId)
        getPublicCompletionImages(challengeId = challengeId)
        getParticipationStatus(challengeId = challengeId)
    }

    fun getChallengeData(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = chalRepository.getChallengeById(id = challengeId)

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body != null) {
                        _state.update { it.copy(challengeData = body.toUi()) }
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

    fun getPublicCompletionImages(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.getPublicCompletionImages(challengeId = challengeId)

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.items != null) {
                        _state.update { it.copy(publicCompletionImages = body.items) }
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

    fun getParticipationStatus(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.getParticipationStatus(challengeId = challengeId)

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.isActive != null && body.completionCount != null) {
                        _state.update {
                            it.copy(
                                isActive = body.isActive,
                                completedCount = body.completionCount
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

    fun startChallenge(challengeId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.startChallenge(challengeId = challengeId)

                if(resp.isSuccessful) {
                    onSuccess()
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

    fun cancelChallenge(challengeId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.cancelChallenge(challengeId = challengeId)

                if(resp.isSuccessful) {
                    onSuccess()
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

    fun completeChallenge(challengeId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.completeChallenge(challengeId = challengeId)

                if(resp.isSuccessful) {
                    onSuccess()
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
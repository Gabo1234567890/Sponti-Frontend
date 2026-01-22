package org.tues.sponti.ui.screens.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.core.AppEvents
import org.tues.sponti.data.chal.ChalRepository
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.part.PartRepository
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.FinishChallengeFlowStep
import org.tues.sponti.ui.screens.common.toUi
import java.io.File

class ChallengeViewModel(
    private val chalRepository: ChalRepository = ChalRepository(),
    private val partRepository: PartRepository = PartRepository(),
    challengeId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ChallengeState())
    val state = _state.asStateFlow()

    init {
        getChallengeData(challengeId = challengeId)
    }

    fun getChallengeData(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = chalRepository.getChallengeById(id = challengeId)

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.challenge != null && body.publicCompletionImages?.items != null && body.status?.isActive != null && body.status.completionCount != null) {
                        _state.update {
                            it.copy(
                                challengeData = body.challenge.toUi(),
                                publicCompletionImages = body.publicCompletionImages.items,
                                isActive = body.status.isActive,
                                completedCount = body.status.completionCount
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

    fun startChallenge(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.startChallenge(challengeId = challengeId)

                if (resp.isSuccessful) {
                    AppEvents.notifyChallengeInteracted()
                    getChallengeData(challengeId = challengeId)
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

    fun cancelChallenge(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val resp = partRepository.cancelChallenge(challengeId = challengeId)

                if (resp.isSuccessful) {
                    AppEvents.notifyChallengeInteracted()
                    getChallengeData(challengeId = challengeId)
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

    fun completeChallenge(challengeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isSubmittingFinish = true) }
            try {
                val completeResp = partRepository.completeChallenge(challengeId = challengeId)

                if (!completeResp.isSuccessful) {
                    val errorJson = completeResp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                    return@launch
                }

                if (_state.value.completionImages.isNotEmpty()) {
                    val uploadedResp = partRepository.uploadImages(
                        challengeId = challengeId,
                        images = _state.value.completionImages
                    )

                    if (!uploadedResp.isSuccessful) {
                        val errorJson = uploadedResp.errorBody()?.string()
                        val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                        val parsed = errorJson?.let { adapter.fromJson(it) }
                        _state.update { it.copy(globalError = parsed?.toFieldError()) }
                        return@launch
                    }
                }

                AppEvents.notifyChallengeInteracted()
                getChallengeData(challengeId = challengeId)
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update {
                    it.copy(
                        isLoading = false,
                        finishStep = FinishChallengeFlowStep.NONE,
                        completionImages = emptyList(),
                        isSubmittingFinish = false
                    )
                }
            }
        }
    }

    fun onFinishStepChange(value: FinishChallengeFlowStep) {
        _state.update { it.copy(finishStep = value) }
    }

    fun onAddCompletionImage(value: File) {
        if (_state.value.completionImages.size < 3) {
            _state.update { it.copy(completionImages = _state.value.completionImages + value) }
        }
    }

    fun onRemoveCompletionImage(value: File) {
        _state.update {
            it.copy(completionImages = it.completionImages - value)
        }
    }

    fun clearGlobalError() {
        _state.update { it.copy(globalError = null) }
    }
}
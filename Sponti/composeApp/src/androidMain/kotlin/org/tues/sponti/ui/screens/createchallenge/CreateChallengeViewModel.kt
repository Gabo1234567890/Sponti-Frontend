package org.tues.sponti.ui.screens.createchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.data.chal.ChalRepository
import org.tues.sponti.data.chal.PlaceType
import org.tues.sponti.data.chal.VehicleType
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.formattedTimeToMinutes
import java.io.File

class CreateChallengeViewModel(
    private val chalRepository: ChalRepository = ChalRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(CreateChallengeState())
    val state = _state.asStateFlow()

    fun onTitleChange(value: String) {
        _state.update { it.copy(title = value) }
    }

    fun onDescriptionChange(value: String) {
        _state.update { it.copy(description = value) }
    }

    fun onThumbnailChange(value: File) {
        _state.update { it.copy(thumbnail = value) }
    }

    fun onPriceChange(value: Int) {
        _state.update { it.copy(price = value) }
    }

    fun onDurationChange(value: String) {
        _state.update { it.copy(duration = value) }
    }

    fun onVehicleChange(value: VehicleType) {
        _state.update { it.copy(vehicle = value) }
    }

    fun onPlaceChange(value: String) {
        _state.update { it.copy(place = value) }
    }

    fun onPlaceTypeChange(value: PlaceType) {
        _state.update { it.copy(placeType = value) }
    }

    fun submit(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            try {
                val resp = chalRepository.submitChallenge(
                    thumbnail = _state.value.thumbnail,
                    title = _state.value.title,
                    description = _state.value.description,
                    price = _state.value.price,
                    durationMinutes = _state.value.duration.formattedTimeToMinutes(),
                    place = _state.value.place,
                    vehicle = _state.value.vehicle,
                    placeType = _state.value.placeType,
                )

                if (resp.isSuccessful) {
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
                _state.update { it.copy(isSubmitting = false) }
            }
        }
    }
}
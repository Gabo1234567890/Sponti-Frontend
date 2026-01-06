package org.tues.sponti.ui.screens.home

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
import org.tues.sponti.ui.screens.common.FilterType
import org.tues.sponti.ui.screens.common.toUi
import kotlin.collections.orEmpty

private const val PLACE_SIGNATURE_ALL = "ALL"

class HomeViewModel(private val chalRepository: ChalRepository = ChalRepository()) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var lastQuerySignature: String? = null

    init {
        fetchChallenges()
    }

    private fun effectivePlaceTypes(selected: Set<PlaceType>): List<PlaceType>? {
        return when (selected) {
            setOf(PlaceType.INDOOR) -> listOf(PlaceType.INDOOR)
            setOf(PlaceType.OUTDOOR) -> listOf(PlaceType.OUTDOOR)
            else -> null
        }
    }

    private fun buildQuerySignature(): String {
        val placeSignature = effectivePlaceTypes(
            _state.value.appliedFilters.filterIsInstance<HomeFilter.Place>()
                .firstOrNull()?.values.orEmpty()
        )?.sorted()?.joinToString() ?: PLACE_SIGNATURE_ALL

        return listOf(
            _state.value.appliedFilters.filterIsInstance<HomeFilter.Price>().firstOrNull()
                ?.toString(),
            _state.value.appliedFilters.filterIsInstance<HomeFilter.Duration>().firstOrNull()
                ?.toString(),
            _state.value.appliedFilters.filterIsInstance<HomeFilter.Vehicle>()
                .firstOrNull()?.values?.sorted()
                ?.joinToString(),
            placeSignature
        ).joinToString("|")
    }

    fun openFilter(type: FilterType) {
        _state.update { it.copy(activePopUp = type) }
    }

    fun closeFilter() {
        _state.update { it.copy(activePopUp = null) }
    }

    fun applyFilter(filter: HomeFilter) {
        _state.update { state ->
            val updatedFilters = when (filter) {
                is HomeFilter.Price -> state.appliedFilters.filterNot { it is HomeFilter.Price } + filter

                is HomeFilter.Duration -> state.appliedFilters.filterNot { it is HomeFilter.Duration } + filter

                is HomeFilter.Vehicle -> {
                    val existing = state.appliedFilters.filterIsInstance<HomeFilter.Vehicle>()
                        .flatMap { it.values }.toSet()
                    val merged = existing + filter.values
                    state.appliedFilters.filterNot { it is HomeFilter.Vehicle } + HomeFilter.Vehicle(
                        merged
                    )
                }

                is HomeFilter.Place -> {
                    val existing = state.appliedFilters.filterIsInstance<HomeFilter.Place>()
                        .flatMap { it.values }.toSet()
                    val merged = existing + filter.values
                    state.appliedFilters.filterNot { it is HomeFilter.Place } + HomeFilter.Place(
                        merged
                    )
                }
            }

            state.copy(
                appliedFilters = updatedFilters, activePopUp = null
            )
        }
        fetchChallenges()
    }

    fun removeFilter(filter: HomeFilter) {
        _state.update { state ->
            val updatedFilters = when (filter) {
                is HomeFilter.Price -> state.appliedFilters.filterNot { it is HomeFilter.Price }

                is HomeFilter.Duration -> state.appliedFilters.filterNot { it is HomeFilter.Duration }

                else -> state.appliedFilters
            }

            state.copy(
                appliedFilters = updatedFilters, activePopUp = null
            )
        }
        fetchChallenges()
    }

    fun removeVehicleFilter(vehicle: VehicleType) {
        _state.update { state ->
            val current = state.appliedFilters.filterIsInstance<HomeFilter.Vehicle>().firstOrNull()

            val updatedValues = current?.values?.minus(vehicle).orEmpty()

            val updatedFilters = state.appliedFilters.filterNot { it is HomeFilter.Vehicle } +
                    (if (updatedValues.isNotEmpty()) HomeFilter.Vehicle(updatedValues) else null)

            state.copy(appliedFilters = updatedFilters.filterNotNull())
        }
        fetchChallenges()
    }

    fun removePlaceTypeFilter(placeType: PlaceType) {
        _state.update { state ->
            val current = state.appliedFilters.filterIsInstance<HomeFilter.Place>().firstOrNull()

            val updatedValues = current?.values?.minus(placeType).orEmpty()

            val updatedFilters = state.appliedFilters.filterNot { it is HomeFilter.Place } +
                    (if (updatedValues.isNotEmpty()) HomeFilter.Place(updatedValues) else null)

            state.copy(appliedFilters = updatedFilters.filterNotNull())
        }
        fetchChallenges()
    }

    fun clearAllFilters() {
        _state.update {
            it.copy(appliedFilters = emptyList())
        }
        fetchChallenges()
    }

    fun fetchChallenges() {
        val signature = buildQuerySignature()
        if (signature == lastQuerySignature) return
        else lastQuerySignature = signature

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val priceFilter =
                    _state.value.appliedFilters.filterIsInstance<HomeFilter.Price>().firstOrNull()
                val durationFilter =
                    _state.value.appliedFilters.filterIsInstance<HomeFilter.Duration>()
                        .firstOrNull()
                val vehicleFilter =
                    _state.value.appliedFilters.filterIsInstance<HomeFilter.Vehicle>().firstOrNull()
                val placeTypeFilter =
                    _state.value.appliedFilters.filterIsInstance<HomeFilter.Place>().firstOrNull()

                val effectivePlaceTypes = effectivePlaceTypes(placeTypeFilter?.values.orEmpty())

                val resp = chalRepository.fetchChallengesByFilters(
                    minPrice = priceFilter?.min,
                    maxPrice = priceFilter?.max,
                    minDuration = durationFilter?.min,
                    maxDuration = durationFilter?.max,
                    vehicles = vehicleFilter?.values?.toList(),
                    placeTypes = effectivePlaceTypes
                )

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.items != null) {
                        _state.update { it.copy(challenges = body.items.map { dto -> dto.toUi() }) }
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
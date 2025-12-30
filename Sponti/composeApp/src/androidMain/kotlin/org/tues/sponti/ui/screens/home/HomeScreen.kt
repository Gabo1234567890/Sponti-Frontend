package org.tues.sponti.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.sponti.R
import org.tues.sponti.ui.components.AppliedFilterChip
import org.tues.sponti.ui.components.ChallengeCard
import org.tues.sponti.ui.components.DimmedOverlay
import org.tues.sponti.ui.components.DropdownFilterPopup
import org.tues.sponti.ui.components.FilterButton
import org.tues.sponti.ui.components.TextFieldsFilterPopup
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.FilterType
import org.tues.sponti.ui.screens.common.PlaceType
import org.tues.sponti.ui.screens.common.VehicleType
import org.tues.sponti.ui.screens.common.minutesToFormattedTimeString
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.screens.home.HomeFilter.*
import org.tues.sponti.ui.theme.Base0

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    val snackBarHostState = remember { SnackbarHostState() }
    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .background(Base0)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                FilterButton(
                    iconId = R.drawable.price,
                    label = "Price",
                    selected = state.activePopUp == FilterType.PRICE
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.PRICE)
                    else viewModel.closeFilter()
                }
                Spacer(Modifier.width(12.dp))
                FilterButton(
                    iconId = R.drawable.time,
                    label = "Time",
                    selected = state.activePopUp == FilterType.DURATION
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.DURATION)
                    else viewModel.closeFilter()
                }
                Spacer(Modifier.width(12.dp))
                FilterButton(
                    iconId = R.drawable.vehicle,
                    label = "Vehicle",
                    selected = state.activePopUp == FilterType.VEHICLE
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.VEHICLE)
                    else viewModel.closeFilter()
                }
                Spacer(Modifier.width(12.dp))
                FilterButton(
                    iconId = R.drawable.location,
                    label = "Place",
                    selected = state.activePopUp == FilterType.PLACE
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.PLACE)
                    else viewModel.closeFilter()
                }
            }
        }
        if (state.activePopUp != null) {
            item {
                DimmedOverlay { viewModel.closeFilter() }

                Spacer(Modifier.height(12.dp))
                when (state.activePopUp) {
                    FilterType.PRICE -> TextFieldsFilterPopup(
                        iconId = R.drawable.price,
                        minLabel = "Min price",
                        maxLabel = "Max price",
                        maxDigits = 3
                    ) { min: Int?, max: Int? -> viewModel.applyFilter(Price(min, max)) }

                    FilterType.DURATION -> TextFieldsFilterPopup(
                        iconId = R.drawable.time,
                        minLabel = "Min time duration",
                        maxLabel = "Max time duration",
                        maxDigits = 3
                    ) { min: Int?, max: Int? ->
                        viewModel.applyFilter(
                            Duration(
                                min, max
                            )
                        )
                    }

                    FilterType.VEHICLE -> DropdownFilterPopup(
                        iconId = R.drawable.vehicle,
                        label = "Vehicles...",
                        options = VehicleType.entries.map { vehicleType -> vehicleType.name.replaceFirstChar { it.uppercase() } }) { selected: Set<String> ->
                        viewModel.applyFilter(Vehicle(selected.map {
                            VehicleType.valueOf(
                                it.uppercase()
                            )
                        }.toSet()))
                    }

                    FilterType.PLACE -> DropdownFilterPopup(
                        iconId = R.drawable.location,
                        label = "Place types...",
                        options = PlaceType.entries.map { placeType -> placeType.name.replaceFirstChar { it.uppercase() } }) { selected: Set<String> ->
                        viewModel.applyFilter(Place(selected.map {
                            PlaceType.valueOf(it.uppercase())
                        }.toSet()))
                    }

                    else -> {}
                }
            }
        }
        item {
            Spacer(Modifier.height(20.dp))
            FlowRow(maxItemsInEachRow = 3, modifier = Modifier.fillMaxWidth()) {
                state.appliedFilters.forEach { filter ->
                    when (filter) {
                        is Price -> AppliedFilterChip(
                            iconId = R.drawable.price,
                            text = "${filter.min ?: 0} - ${filter.max ?: 999}"
                        ) { viewModel.removeFilter(filter) }

                        is Duration -> AppliedFilterChip(
                            iconId = R.drawable.time,
                            text = "${filter.min?.minutesToFormattedTimeString() ?: 0.minutesToFormattedTimeString()} - ${filter.max?.minutesToFormattedTimeString() ?: "24:00"}"
                        ) { viewModel.removeFilter(filter) }

                        is Vehicle -> filter.values.forEach { vehicleType ->
                            AppliedFilterChip(
                                iconId = R.drawable.vehicle,
                                text = vehicleType.name.replaceFirstChar { it.uppercase() }) {
                                viewModel.removeFilter(
                                    filter
                                )
                            }
                        }

                        is Place -> filter.values.forEach { placeType ->
                            AppliedFilterChip(
                                iconId = R.drawable.location,
                                text = placeType.name.replaceFirstChar { it.uppercase() }) {
                                viewModel.removeFilter(
                                    filter
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(36.dp))
        }

        items(items = state.challenges) {
            ChallengeCard(it) {
                navController.navigate("challenge/${it.id}")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
package org.tues.sponti.ui.screens.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.tues.sponti.R
import org.tues.sponti.data.chal.PlaceType
import org.tues.sponti.data.chal.VehicleType
import org.tues.sponti.navigation.ProtectedScaffold
import org.tues.sponti.ui.components.DimmedOverlay
import org.tues.sponti.ui.components.DropdownFilterPopup
import org.tues.sponti.ui.components.TextFieldsFilterPopup
import org.tues.sponti.ui.screens.common.FilterType
import org.tues.sponti.ui.screens.home.HomeFilter.Duration
import org.tues.sponti.ui.screens.home.HomeFilter.Place
import org.tues.sponti.ui.screens.home.HomeFilter.Price
import org.tues.sponti.ui.screens.home.HomeFilter.Vehicle

@Composable
fun HomeRoute(navController: NavHostController) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProtectedScaffold(navController) { paddingValues ->
            HomeScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues = paddingValues),
                viewModel = viewModel
            )
        }

        if (state.activePopUp != null) {
            DimmedOverlay(
                modifier = Modifier.zIndex(1f),
                onDismiss = { viewModel.closeFilter() }
            )

            Box(
                modifier = Modifier
                    .pointerInput(Unit) { detectTapGestures { } }
                    .padding(start = 16.dp, end = 16.dp, top = 160.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(2f)
            ) {
                state.activePopUp?.let { active ->
                    when (active) {
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
                            options = VehicleType.entries.map { vehicleType ->
                                vehicleType.name.lowercase().replaceFirstChar { it.uppercase() }
                            }) { selected: Set<String> ->
                            viewModel.applyFilter(Vehicle(selected.map {
                                VehicleType.valueOf(
                                    it.uppercase()
                                )
                            }.toSet()))
                        }

                        FilterType.PLACE -> DropdownFilterPopup(
                            iconId = R.drawable.location,
                            label = "Place types...",
                            options = PlaceType.entries.map { placeType ->
                                placeType.name.lowercase().replaceFirstChar { it.uppercase() }
                            }) { selected: Set<String> ->
                            viewModel.applyFilter(Place(selected.map {
                                PlaceType.valueOf(it.uppercase())
                            }.toSet()))
                        }
                    }
                }
            }
        }
    }
}
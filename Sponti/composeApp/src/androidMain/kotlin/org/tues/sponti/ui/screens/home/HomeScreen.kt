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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import org.tues.sponti.ui.components.FilterButton
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.FilterType
import org.tues.sponti.ui.screens.common.minutesToFormattedTimeString
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.screens.home.HomeFilter.*
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Primary1

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
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
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .background(Base0)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(24.dp)) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.price,
                    label = "Price"
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.PRICE)
                    else viewModel.closeFilter()
                }
                FilterButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.time,
                    label = "Time"
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.DURATION)
                    else viewModel.closeFilter()
                }
                FilterButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.vehicle,
                    label = "Vehicle"
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.VEHICLE)
                    else viewModel.closeFilter()
                }
                FilterButton(
                    modifier = Modifier.weight(1f),
                    iconId = R.drawable.location,
                    label = "Place"
                ) {
                    if (state.activePopUp == null) viewModel.openFilter(FilterType.PLACE)
                    else viewModel.closeFilter()
                }
            }
        }
        if (state.appliedFilters.isNotEmpty()) {
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
            }
        }

        item { Spacer(Modifier.height(36.dp)) }

        when {
            state.isLoading -> item {
                Text(
                    text = "Loading...",
                    style = Heading4,
                    color = Primary1
                )
            }

            state.challenges.isEmpty() -> {
                item {
                    Text(text = "No challenges found.", style = Heading4, color = Primary1)
                }
            }

            else -> {
                items(items = state.challenges) {
                    ChallengeCard(it) {
                        navController.navigate("challenge/${it.id}")
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }

}
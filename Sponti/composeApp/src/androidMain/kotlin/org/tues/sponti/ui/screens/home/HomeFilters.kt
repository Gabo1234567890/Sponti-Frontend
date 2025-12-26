package org.tues.sponti.ui.screens.home

import org.tues.sponti.ui.screens.common.PlaceType
import org.tues.sponti.ui.screens.common.VehicleType

sealed class HomeFilter {
    data class Price(val min: Int?, val max: Int?) : HomeFilter()
    data class Duration(val min: Int?, val max: Int?) : HomeFilter()
    data class Vehicle(val values: Set<VehicleType>) : HomeFilter()
    data class Place(val values: Set<PlaceType>) : HomeFilter()
}
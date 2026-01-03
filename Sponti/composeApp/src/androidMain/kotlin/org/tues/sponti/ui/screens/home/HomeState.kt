package org.tues.sponti.ui.screens.home

import org.tues.sponti.ui.screens.common.ChallengeType
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.FilterType

data class HomeState(
    val activePopUp: FilterType? = null,
    val appliedFilters: List<HomeFilter> = emptyList(),
    val challenges: List<ChallengeType> = emptyList(),
    val isLoading: Boolean = false,
    val globalError: FieldError? = null
)
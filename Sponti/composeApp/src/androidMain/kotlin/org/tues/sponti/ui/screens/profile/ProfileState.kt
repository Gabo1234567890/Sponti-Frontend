package org.tues.sponti.ui.screens.profile

import org.tues.sponti.data.user.MemoryItemDto
import org.tues.sponti.data.user.UserDto
import org.tues.sponti.ui.screens.common.ChallengeType
import org.tues.sponti.ui.screens.common.FieldError

data class ProfileState(
    val userData: UserDto? = null,
    val activeChallenge: List<ChallengeType> = emptyList(),
    val completedCount: Int = 0,
    val memories: List<MemoryItemDto> = emptyList(),
    val isLoading: Boolean = false,
    val globalError: FieldError? = null
)
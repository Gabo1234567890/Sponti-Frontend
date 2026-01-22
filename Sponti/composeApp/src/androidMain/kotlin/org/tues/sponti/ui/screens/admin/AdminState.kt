package org.tues.sponti.ui.screens.admin

import org.tues.sponti.data.network.AdminChallengeListItem
import org.tues.sponti.data.network.AdminUserListItem
import org.tues.sponti.ui.screens.common.FieldError

data class AdminState(
    val selectedTab: AdminTab = AdminTab.USERS,
    val users: List<AdminUserListItem> = emptyList(),
    val challenges: List<AdminChallengeListItem> = emptyList(),
    val pageUsers: Int = 1,
    val pageApprovedChallenges: Int = 1,
    val pagePendingChallenges: Int = 1,
    val totalPagesUsers: Int = 1,
    val totalPagesApprovedChallenges: Int = 1,
    val totalPagesPendingChallenges: Int = 1,
    val showConfirmationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val globalError: FieldError? = null
)
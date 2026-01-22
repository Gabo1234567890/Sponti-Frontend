package org.tues.sponti.ui.screens.admin

import org.tues.sponti.data.user.Role

sealed interface AdminConfirmAction {
    data class ChangeRole(val userId: String, val role: Role) : AdminConfirmAction
    data class DeleteUser(val userId: String) : AdminConfirmAction
    data class ApproveChallenge(val challengeId: String) : AdminConfirmAction
    data class DeleteChallenge(val challengeId: String, val approved: Boolean) : AdminConfirmAction
}

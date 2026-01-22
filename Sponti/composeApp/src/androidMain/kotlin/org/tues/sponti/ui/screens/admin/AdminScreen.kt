package org.tues.sponti.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.sponti.R
import org.tues.sponti.navigation.Routes
import org.tues.sponti.ui.components.AdminTabButton
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.ChallengesTable
import org.tues.sponti.ui.components.PaginationBar
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.components.UsersTable
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Paragraph1

@Composable
fun AdminScreen(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: AdminViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    var confirmAction by remember { mutableStateOf<AdminConfirmAction?>(null) }
    var confirmTitle by remember { mutableStateOf("") }
    var confirmText by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearGlobalError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Base0)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = false,
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
            ) {
                AdminTabButton(
                    text = "Users",
                    selected = state.selectedTab == AdminTab.USERS,
                    position = AdminTabButtonPosition.LEFT,
                    modifier = Modifier.width(100.dp)
                ) {
                    viewModel.onSelectedTabChange(AdminTab.USERS)
                    viewModel.loadUsers()
                }
                AdminTabButton(
                    text = "Approved",
                    selected = state.selectedTab == AdminTab.APPROVED_CHALLENGES,
                    position = AdminTabButtonPosition.MIDDLE,
                    modifier = Modifier.width(100.dp)
                ) {
                    viewModel.onSelectedTabChange(AdminTab.APPROVED_CHALLENGES)
                    viewModel.loadChallenges(approved = true)
                }
                AdminTabButton(
                    text = "Pending",
                    selected = state.selectedTab == AdminTab.PENDING_CHALLENGES,
                    position = AdminTabButtonPosition.RIGHT,
                    modifier = Modifier.width(100.dp)
                ) {
                    viewModel.onSelectedTabChange(AdminTab.PENDING_CHALLENGES)
                    viewModel.loadChallenges(approved = false)
                }
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                when (state.selectedTab) {
                    AdminTab.USERS -> if (state.users.isNotEmpty()) Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UsersTable(
                            users = state.users,
                            onRoleChange = { userId, role ->
                                confirmTitle = "Change role"
                                confirmText = "Change this user's role?"
                                confirmAction = AdminConfirmAction.ChangeRole(userId, role)
                            },
                            onDelete = { userId ->
                                confirmTitle = "Delete user"
                                confirmText = "Are you sure? This action cannot be undone."
                                confirmAction = AdminConfirmAction.DeleteUser(userId)
                            }
                        )
                        PaginationBar(
                            page = state.pageUsers,
                            totalPages = state.totalPagesUsers,
                            onPrev = { viewModel.onPageUsersChange(state.pageUsers - 1) },
                            onNext = { viewModel.onPageUsersChange(state.pageUsers + 1) }
                        )
                    } else Text(text = "No results", style = Heading4, color = Base100)

                    AdminTab.APPROVED_CHALLENGES -> if (state.challenges.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ChallengesTable(
                                challenges = state.challenges,
                                onDelete = { chalId ->
                                    confirmTitle = "Delete challenge"
                                    confirmText = "Are you sure? This action cannot be undone."
                                    confirmAction =
                                        AdminConfirmAction.DeleteChallenge(
                                            challengeId = chalId,
                                            approved = true
                                        )
                                },
                                onNavigate = { chalId ->
                                    navController.navigate("${Routes.CHALLENGE}/${chalId}")
                                }
                            )
                            PaginationBar(
                                page = state.pageApprovedChallenges,
                                totalPages = state.totalPagesApprovedChallenges,
                                onPrev = { viewModel.onPageApprovedChallengesChange(state.pageApprovedChallenges - 1) },
                                onNext = { viewModel.onPageApprovedChallengesChange(state.pageApprovedChallenges + 1) }
                            )
                        }
                    } else Text(text = "No results", style = Heading4, color = Base100)

                    AdminTab.PENDING_CHALLENGES -> if (state.challenges.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ChallengesTable(
                                challenges = state.challenges,
                                onApprove = { chalId ->
                                    confirmTitle = "Approve challenge"
                                    confirmText = "Approve this challenge?"
                                    confirmAction =
                                        AdminConfirmAction.ApproveChallenge(challengeId = chalId)
                                },
                                onDelete = { chalId ->
                                    confirmTitle = "Deny challenge"
                                    confirmText = "Are you sure? This action cannot be undone."
                                    confirmAction = AdminConfirmAction.DeleteChallenge(
                                        challengeId = chalId,
                                        approved = false
                                    )
                                },
                                onNavigate = { chalId ->
                                    navController.navigate("${Routes.CHALLENGE}/${chalId}")
                                }
                            )
                            PaginationBar(
                                page = state.pagePendingChallenges,
                                totalPages = state.totalPagesPendingChallenges,
                                onPrev = { viewModel.onPagePendingChallengesChange(state.pagePendingChallenges - 1) },
                                onNext = { viewModel.onPagePendingChallengesChange(state.pagePendingChallenges + 1) }
                            )
                        }
                    } else Text(text = "No results", style = Heading4, color = Base100)
                }
            }

            if (confirmAction != null) {
                AlertDialog(
                    onDismissRequest = { confirmAction = null },
                    title = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = { confirmAction = null },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.cross),
                                    contentDescription = "Close",
                                    tint = Base100,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = confirmTitle,
                                style = Heading4,
                                color = Base100,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = confirmText,
                                style = Paragraph1,
                                color = Base80,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    confirmButton = {
                        PrimaryButton(
                            text = "Confirm",
                            size = ButtonSize.Large,
                            state = ButtonState.Active
                        ) {
                            when (val action = confirmAction) {
                                is AdminConfirmAction.ChangeRole -> viewModel.changeUserRole(
                                    id = action.userId,
                                    role = action.role
                                )

                                is AdminConfirmAction.DeleteUser -> viewModel.deleteUser(id = action.userId)

                                is AdminConfirmAction.ApproveChallenge -> {
                                    viewModel.approveChallenge(
                                        id = action.challengeId
                                    )
                                }

                                is AdminConfirmAction.DeleteChallenge -> {
                                    viewModel.deleteChallenge(
                                        id = action.challengeId,
                                        approved = action.approved
                                    )
                                }

                                null -> Unit
                            }
                            confirmAction = null
                        }
                    },
                    dismissButton = {
                        PrimaryButton(
                            text = "Cancel",
                            size = ButtonSize.Large,
                            state = ButtonState.Active,
                            color = Base80
                        ) {
                            confirmAction = null
                        }
                    },
                    properties = DialogProperties(dismissOnClickOutside = true)
                )
            }
        }
    }
}
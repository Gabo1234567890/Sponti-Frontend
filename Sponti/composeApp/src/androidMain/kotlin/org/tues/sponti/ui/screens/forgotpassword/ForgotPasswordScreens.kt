package org.tues.sponti.ui.screens.forgotpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import org.tues.sponti.R
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.ui.components.FlowTopBar
import org.tues.sponti.ui.screens.common.CheckInboxScreen
import org.tues.sponti.ui.screens.common.FlowType
import org.tues.sponti.ui.screens.common.StatusScreen
import org.tues.sponti.ui.screens.common.flowTexts

@Composable
fun ForgotPasswordScreens(
    onBackToLogin: () -> Unit,
    token: String = "",
    email: String = "",
    backStackEntry: NavBackStackEntry
) {
    val viewModel: ForgotPasswordViewModel =
        viewModel(factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ForgotPasswordViewModel(
                    authRepository = AuthRepository(),
                    savedStateHandle = backStackEntry.savedStateHandle
                ) as T
            }
        })
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val texts = flowTexts(FlowType.FORGOT_PASSWORD)

    Scaffold(topBar = {
        FlowTopBar(onBack = {
            onBackToLogin()
            viewModel.onStepChange(ForgotPasswordStep.ENTER_EMAIL)
        })
    }) { paddingValues ->

        when (state.step) {
            ForgotPasswordStep.ENTER_EMAIL -> ForgotPasswordEmailScreen(
                viewModel = viewModel,
                state = state,
                snackBarHostState = snackBarHostState,
                modifier = Modifier.padding(paddingValues)
            )

            ForgotPasswordStep.CHECK_INBOX -> {
                CheckInboxScreen(
                    title = texts.title,
                    inboxMessage = texts.inboxMessage,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            ForgotPasswordStep.RESET_PASSWORD -> ForgotPasswordNewPassScreen(
                viewModel = viewModel,
                state = state,
                snackBarHostState = snackBarHostState,
                token = token,
                email = email,
                modifier = Modifier.padding(paddingValues)
            )

            ForgotPasswordStep.SUCCESS -> StatusScreen(
                title = texts.title,
                iconId = R.drawable.tick,
                headline = texts.successTitle,
                description = texts.successMessage,
                buttonText = "Continue",
                onClick = {
                    onBackToLogin()
                    viewModel.onStepChange(ForgotPasswordStep.ENTER_EMAIL)
                },
                modifier = Modifier.padding(paddingValues)
            )

            ForgotPasswordStep.FAILURE -> StatusScreen(
                title = texts.title,
                iconId = R.drawable.cross,
                headline = texts.failureTitle,
                description = texts.failureMessage,
                buttonText = "Go Back",
                onClick = {
                    onBackToLogin()
                    viewModel.onStepChange(ForgotPasswordStep.ENTER_EMAIL)
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
package org.tues.sponti.ui.screens.verifyemail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Heading1

@Composable
fun VerifyEmailScreens(
    onBackToLogin: () -> Unit,
    backStackEntry: NavBackStackEntry
) {
    val viewModel: VerifyEmailViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VerifyEmailViewModel(
                authRepository = AuthRepository(),
                savedStateHandle = backStackEntry.savedStateHandle
            ) as T
        }
    })
    val state by viewModel.state.collectAsState()
    val texts = flowTexts(FlowType.VERIFY_EMAIL)

    Scaffold(topBar = {
        FlowTopBar(onBack = {
            onBackToLogin()
        })
    }) { paddingValues ->
        when (state.step) {
            VerifyEmailStep.CHECK_INBOX -> CheckInboxScreen(
                title = texts.title,
                inboxMessage = texts.inboxMessage,
                modifier = Modifier.padding(paddingValues)
            )

            VerifyEmailStep.VERIFYING -> {
                Text(text = "Loading...", style = Heading1, color = Base100)
            }

            VerifyEmailStep.SUCCESS -> StatusScreen(
                title = texts.title,
                iconId = R.drawable.tick,
                headline = texts.successTitle,
                description = texts.successMessage,
                buttonText = "Continue",
                onClick = {
                    onBackToLogin()
                },
                modifier = Modifier.padding(paddingValues)
            )

            VerifyEmailStep.FAILURE -> StatusScreen(
                title = texts.title,
                iconId = R.drawable.cross,
                headline = texts.failureTitle,
                description = texts.failureMessage,
                buttonText = "Go Back",
                onClick = {
                    onBackToLogin()
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
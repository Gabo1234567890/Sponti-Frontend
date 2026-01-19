package org.tues.sponti.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.tues.sponti.R
import org.tues.sponti.SpontiApp
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.data.user.UserRepository
import org.tues.sponti.navigation.ProtectedScaffold
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.DimmedOverlay
import org.tues.sponti.ui.components.InputField
import org.tues.sponti.ui.components.InputState
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base40
import org.tues.sponti.ui.theme.Body2

@Composable
fun SettingsRoute(navController: NavHostController, main: Boolean) {
    val app = LocalContext.current.applicationContext as SpontiApp

    val viewModel: SettingsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(
                authRepository = AuthRepository(),
                userRepository = UserRepository(),
                sessionManager = app.sessionManager
            ) as T
        }
    })

    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    var usernameState by remember { mutableStateOf(InputState.Filled) }

    LaunchedEffect(state.usernameError) {
        usernameState = if (state.usernameError != null) InputState.Error
        else if (state.username.isEmpty()) InputState.Default
        else InputState.Filled
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ProtectedScaffold(navController, main) { paddingValues ->
            SettingsScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }

        if (state.viewAccountDetails) {
            DimmedOverlay(
                modifier = Modifier.zIndex(1f),
                onDismiss = {
                    viewModel.onViewAccountDetailsChange(false)
                    viewModel.onUsernameChange(state.originalUsername)
                })

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(2f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Base0,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .border(border = BorderStroke(width = 1.dp, color = Base40))
                        .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                        .padding(all = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Account Details", style = Body2, color = Base100)
                    InputField(
                        value = state.username,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        label = "Username",
                        placeholder = "Username",
                        inputState = usernameState,
                        errorMessage = state.usernameError?.toUiText(FieldType.USERNAME) ?: "",
                        showIcon = true,
                        icon = {
                            Image(
                                painter = painterResource(R.drawable.pencil),
                                contentDescription = "Edit"
                            )
                        },
                        onIconClick = { usernameState = InputState.Active },
                        focusOnIconClick = true,
                        onFocusChange = { focused ->
                            usernameState =
                                if (focused) InputState.Active else if (state.username.isEmpty()) InputState.Default else InputState.Filled
                        })
                    InputField(
                        value = state.email,
                        onValueChange = {},
                        label = "Email",
                        placeholder = "",
                        inputState = InputState.Filled,
                        readOnly = true,
                        onFocusChange = {})
                    PrimaryButton(
                        text = "Confirm",
                        size = ButtonSize.Large,
                        state = if (state.username.isEmpty() || state.usernameError != null) ButtonState.Disabled else ButtonState.Active
                    ) {
                        focusManager.clearFocus()
                        viewModel.patchUserData(username = state.username)
                        usernameState =
                            if (state.usernameError == null) if (state.username.isEmpty()) InputState.Default else InputState.Filled else InputState.Error
                    }
                }
            }
        }
    }
}
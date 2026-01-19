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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.sponti.R
import org.tues.sponti.SpontiApp
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.data.user.Role
import org.tues.sponti.data.user.UserRepository
import org.tues.sponti.navigation.Routes
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.DimmedOverlay
import org.tues.sponti.ui.components.InputField
import org.tues.sponti.ui.components.InputState
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.components.SettingElement
import org.tues.sponti.ui.components.Switch
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base40
import org.tues.sponti.ui.theme.Body2
import org.tues.sponti.ui.theme.Error
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Primary1

@Composable
fun SettingsScreen(navController: NavController, modifier: Modifier = Modifier) {
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

    val snackBarHostState = remember { SnackbarHostState() }

    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(state.usernameError) {
        usernameState = if (state.usernameError != null) InputState.Error
        else if (state.username.isEmpty()) InputState.Default
        else InputState.Filled
    }

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            Text(
                text = "Loading...", style = Heading4, color = Primary1
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    SettingElement(title = "Account Details") {
                        viewModel.onViewAccountDetailsChange(
                            true
                        )
                    }
                    HorizontalDivider(thickness = 1.dp, color = Base40)
                    SettingElement(
                        title = "Share Images", action = {
                            Switch(checked = state.allowPublicImages) {
                                viewModel.patchUserData(allowPublicImages = it)
                            }
                        }) {
                        viewModel.patchUserData(
                            allowPublicImages = !state.allowPublicImages
                        )
                    }
                    if (state.role == Role.ADMIN) {
                        HorizontalDivider(thickness = 1.dp, color = Base40)
                        SettingElement(title = "Admin Panel") { }
                    }
                    HorizontalDivider(thickness = 1.dp, color = Base40)
                    SettingElement(title = "Reset Password") {
                        viewModel.logout {
                            navController.navigate(
                                Routes.FORGOT_PASSWORD
                            ) { popUpTo(0) }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = Base40)
                    SettingElement(
                        title = "Logout", titleColor = Error
                    ) { viewModel.logout { navController.navigate(Routes.LOGIN) { popUpTo(0) } } }
                    HorizontalDivider(thickness = 1.dp, color = Base40)
                    SettingElement(
                        title = "Delete Account", titleColor = Error
                    ) {
                        viewModel.deleteAccount {
                            navController.navigate(Routes.CREATE_ACCOUNT) { popUpTo(0) }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = Base40)
                }
                Column {
                    SettingElement(
                        title = "Terms & Conditions",
                        action = {
                            Image(
                                painter = painterResource(R.drawable.back_arrow),
                                contentDescription = null,
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    ) { }
                    SettingElement(
                        title = "Privacy Policy",
                        action = {
                            Image(
                                painter = painterResource(R.drawable.back_arrow),
                                contentDescription = null,
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    ) { }
                }
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
}
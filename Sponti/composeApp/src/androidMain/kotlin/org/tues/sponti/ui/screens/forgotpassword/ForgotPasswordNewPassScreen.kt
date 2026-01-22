package org.tues.sponti.ui.screens.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.InputField
import org.tues.sponti.ui.components.InputState
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.components.StepIndicator
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Primary1

@Composable
fun ForgotPasswordNewPassScreen(
    viewModel: ForgotPasswordViewModel,
    state: ForgotPasswordState,
    snackBarHostState: SnackbarHostState,
    token: String,
    email: String,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var passwordState by remember { mutableStateOf(InputState.Default) }
    var newPasswordState by remember { mutableStateOf(InputState.Default) }

    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    var showPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.passwordError) {
        passwordState = if (state.passwordError != null) InputState.Error
        else if (state.email.isEmpty()) InputState.Default
        else InputState.Filled
    }

    LaunchedEffect(state.newPasswordError) {
        newPasswordState = if (state.newPasswordError != null) InputState.Error
        else if (state.email.isEmpty()) InputState.Default
        else InputState.Filled
    }

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearGlobalError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .background(Base0)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_name),
                    contentDescription = "Sponti Logo"
                )
                Spacer(Modifier.height(12.dp))
                Text(text = "Forgot Password", style = Heading3, color = Primary1)
            }
            Spacer(Modifier.height(64.dp))
            InputField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Password",
                placeholder = "Password",
                inputState = passwordState,
                showIcon = true,
                icon = {
                    Image(
                        painter = painterResource(if (showPassword) R.drawable.eye_crossed else R.drawable.eye),
                        contentDescription = "Toggle password visibility"
                    )
                },
                errorMessage = state.passwordError?.toUiText(FieldType.PASSWORD) ?: "",
                isPassword = true,
                isPasswordVisible = showPassword,
                onIconClick = { showPassword = !showPassword },
                onFocusChange = { focused ->
                    passwordState =
                        if (focused) InputState.Active else if (state.password.isEmpty()) InputState.Default else InputState.Filled
                })
            Spacer(Modifier.height(16.dp))
            InputField(
                value = state.newPassword,
                onValueChange = { viewModel.onNewPasswordChange(it) },
                label = "New password",
                placeholder = "New password",
                inputState = newPasswordState,
                showIcon = true,
                icon = {
                    Image(
                        painter = painterResource(if (showNewPassword) R.drawable.eye_crossed else R.drawable.eye),
                        contentDescription = "Toggle password visibility"
                    )
                },
                errorMessage = state.newPasswordError?.toUiText(FieldType.PASSWORD) ?: "",
                isPassword = true,
                isPasswordVisible = showNewPassword,
                onIconClick = { showNewPassword = !showNewPassword },
                onFocusChange = { focused ->
                    newPasswordState =
                        if (focused) InputState.Active else if (state.newPassword.isEmpty()) InputState.Default else InputState.Filled
                })
            Spacer(Modifier.height(64.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StepIndicator(3, 2)
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = "Reset Password",
                    size = ButtonSize.Large,
                    state = if (
                        state.isLoading
                        || state.password.isEmpty()
                        || state.passwordError != null
                        || state.newPassword.isEmpty()
                        || state.newPasswordError != null
                    ) ButtonState.Disabled
                    else ButtonState.Active
                ) {
                    focusManager.clearFocus()
                    viewModel.resetPassword(
                        token = token,
                        email = email
                    )
                }
            }
        }
    }
}
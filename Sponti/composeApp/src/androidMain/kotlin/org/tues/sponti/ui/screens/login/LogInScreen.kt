package org.tues.sponti.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.tues.sponti.R
import org.tues.sponti.SpontiApp
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.InputField
import org.tues.sponti.ui.components.InputState
import org.tues.sponti.ui.components.LinkButton
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Primary1

@Composable
fun LogInScreen(
    onSuccess: () -> Unit,
    onNavigateToCreateAccount: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val app = LocalContext.current.applicationContext as SpontiApp

    val viewModel: LoginViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(factory = object :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(
                    authRepository = AuthRepository(), sessionManager = app.sessionManager
                ) as T
            }
        })
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    var emailState by remember { mutableStateOf(InputState.Default) }
    var passwordState by remember { mutableStateOf(InputState.Default) }

    var showPassword by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(state.emailError) {
        emailState = if (state.emailError != null) InputState.Error
        else if (state.email.isEmpty()) InputState.Default
        else InputState.Filled
    }

    LaunchedEffect(state.passwordError) {
        passwordState = if (state.passwordError != null) InputState.Error
        else if (state.password.isEmpty()) InputState.Default
        else InputState.Filled
    }

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .background(Base0),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_name),
                contentDescription = "Sponti Logo"
            )
            Spacer(Modifier.height(12.dp))
            Text(text = "Log In", style = Heading3, color = Primary1)
        }
        Spacer(Modifier.height(64.dp))
        Column {
            InputField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = "Email",
                placeholder = "Email",
                inputState = emailState,
                errorMessage = state.emailError?.toUiText(FieldType.EMAIL) ?: "",
                onFocusChange = { focused ->
                    emailState =
                        if (focused) InputState.Active else if (state.email.isEmpty()) InputState.Default else InputState.Filled
                })
            Spacer(Modifier.height(16.dp))
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
                },
                forgotPassword = true,
                onForgotPasswordClick = onNavigateToForgotPassword
            )
        }
        Spacer(Modifier.height(64.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PrimaryButton(
                text = "Log In",
                size = ButtonSize.Large,
                state = if (state.isSubmitting || state.email.isEmpty() || state.password.isEmpty() || state.emailError != null || state.passwordError != null) ButtonState.Disabled
                else ButtonState.Active
            ) {
                focusManager.clearFocus()
                viewModel.submit(onSuccess = onSuccess)
                emailState =
                    if (state.emailError == null) if (state.email.isEmpty()) InputState.Default else InputState.Filled else InputState.Error
                passwordState =
                    if (state.passwordError == null) if (state.password.isEmpty()) InputState.Default else InputState.Filled else InputState.Error
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account?", style = Caption1, color = Base100)
                Spacer(Modifier.width(12.dp))
                LinkButton(text = "Sign Up") { onNavigateToCreateAccount() }
            }
        }
        SnackbarHost(hostState = snackBarHostState)
    }
}
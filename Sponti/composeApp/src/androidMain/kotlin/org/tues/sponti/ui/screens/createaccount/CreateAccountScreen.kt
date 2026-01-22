package org.tues.sponti.ui.screens.createaccount

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
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
fun CreateAccountScreen(
    onNavigateToLogin: () -> Unit, onSuccess: () -> Unit
) {
    val viewModel: CreateAccountViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    var usernameState by remember { mutableStateOf(InputState.Default) }
    var emailState by remember { mutableStateOf(InputState.Default) }
    var passwordState by remember { mutableStateOf(InputState.Default) }

    var showPassword by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(state.usernameError) {
        usernameState = if (state.usernameError != null) InputState.Error
        else if (state.username.isEmpty()) InputState.Default
        else InputState.Filled
    }

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
            viewModel.clearGlobalError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
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
                Text(text = "Create Account", style = Heading3, color = Primary1)
            }
            Spacer(Modifier.height(64.dp))
            Column {
                InputField(
                    value = state.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = "Username",
                    placeholder = "Username",
                    inputState = usernameState,
                    errorMessage = state.usernameError?.toUiText(FieldType.USERNAME) ?: "",
                    onFocusChange = { focused ->
                        usernameState =
                            if (focused) InputState.Active else if (state.username.isEmpty()) InputState.Default else InputState.Filled
                    })
                Spacer(Modifier.height(16.dp))
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
                    })
            }
            Spacer(Modifier.height(64.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PrimaryButton(
                    text = if (state.isSubmitting) "Creating..." else "Create Account",
                    size = ButtonSize.Large,
                    state = if (
                        state.isSubmitting
                        || state.username.isEmpty()
                        || state.email.isEmpty()
                        || state.password.isEmpty()
                        || state.usernameError != null
                        || state.emailError != null
                        || state.passwordError != null
                    ) ButtonState.Disabled
                    else ButtonState.Active
                ) {
                    focusManager.clearFocus()
                    viewModel.submit(onSuccess = onSuccess)
                    usernameState =
                        if (state.usernameError == null) if (state.username.isEmpty()) InputState.Default else InputState.Filled else InputState.Error
                    emailState =
                        if (state.emailError == null) if (state.email.isEmpty()) InputState.Default else InputState.Filled else InputState.Error
                    passwordState =
                        if (state.passwordError == null) if (state.password.isEmpty()) InputState.Default else InputState.Filled else InputState.Error
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Already have an account?", style = Caption1, color = Base100)
                    Spacer(Modifier.width(12.dp))
                    LinkButton(text = "Log In") { onNavigateToLogin() }
                }
            }
        }
    }
}
package org.tues.sponti.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.tues.sponti.R
import org.tues.sponti.data.auth.AuthRepository
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.InputField
import org.tues.sponti.ui.components.InputState
import org.tues.sponti.ui.components.LinkButton
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Primary1

fun String.containsAllCharacterTypes(): Boolean {
    val hasLowercase = Regex("[a-z]").containsMatchIn(this)
    val hasUppercase = Regex("[A-Z]").containsMatchIn(this)
    val hasDigit = Regex("\\d").containsMatchIn(this)
    val hasSpecialChar = Regex("[^A-Za-z0-9]").containsMatchIn(this)

    return hasLowercase && hasUppercase && hasDigit && hasSpecialChar
}

@Composable
fun CreateAccountScreen(
    authRepository: AuthRepository = AuthRepository(),
    onNavigateToLogin: () -> Unit,
    onSuccess: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameState by remember { mutableStateOf(InputState.Default) }
    var emailState by remember { mutableStateOf(InputState.Default) }
    var passwordState by remember { mutableStateOf(InputState.Default) }

    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column {
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
                value = username,
                onValueChange = {
                    username = it
                    usernameError = ""
                },
                label = "Username",
                placeholder = "Username",
                inputState = usernameState,
                errorMessage = usernameError
            )
            Spacer(Modifier.height(16.dp))
            InputField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = "Email",
                placeholder = "Email",
                inputState = emailState,
                errorMessage = emailError
            )
            Spacer(Modifier.height(16.dp))
            InputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = "Password",
                placeholder = "Password",
                inputState = passwordState,
                showIcon = true,
                icon = { if (showPassword) R.drawable.eye_crossed else R.drawable.eye },
                errorMessage = passwordError
            )
        }
        Spacer(Modifier.height(64.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PrimaryButton(
                text = if (isSubmitting) "Creating..." else "Create Account",
                size = ButtonSize.Large,
                state = if (isSubmitting) ButtonState.Disabled else ButtonState.Active
            ) {
                focusManager.clearFocus()
                usernameError = ""
                emailError = ""
                passwordError = ""
                when {
                    username.isBlank() -> usernameError = "Username is empty"

                    !email.contains("@") || !email.contains(".") -> emailError =
                        "Invalid email address"

                    password.length < 8 -> passwordError = "Password must be at least 8 characters"

                    !password.containsAllCharacterTypes() -> passwordError =
                        "Password must contain uppercase, lowercase, number and symbol"

                    else -> {
                        isSubmitting = true
                        scope.launch {
                            try {
                                val resp = authRepository.signup(
                                    username.trim(), email.trim(), password.trim()
                                )
                                if (resp.isSuccessful) {
                                    onSuccess()
                                } else {
                                    val error = resp.errorBody()?.string() ?: "Registration failed"
                                    if ("username" in error) usernameError = error
                                    else if ("email" in error) emailError = error
                                    else if ("password" in error) passwordError = error
                                    else SnackbarHostState().showSnackbar(message = error)
                                }
                            } catch (e: Exception) {
                                val error = e.localizedMessage ?: "Network error"
                                SnackbarHostState().showSnackbar(message = error)
                            } finally {
                                isSubmitting = false
                            }
                        }
                    }
                }
                usernameState = if (usernameError.isEmpty()) InputState.Default else InputState.Error
                emailState = if (emailError.isEmpty()) InputState.Default else InputState.Error
                passwordState = if (passwordError.isEmpty()) InputState.Default else InputState.Error
            }
            Spacer(Modifier.height(8.dp))
            Row {
                Text(text = "Already have an account?", style = Caption1, color = Base100)
                Spacer(Modifier.width(12.dp))
                LinkButton(text = "Log In") { onNavigateToLogin() }
            }
        }
    }
}
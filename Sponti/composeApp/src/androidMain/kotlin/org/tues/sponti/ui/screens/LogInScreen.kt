package org.tues.sponti.ui.screens

import androidx.compose.runtime.Composable
import org.tues.sponti.data.auth.AuthRepository

@Composable
fun LogInScreen(
    authRepository: AuthRepository = AuthRepository(),
    onSuccess: () -> Unit,
    onNavigateToCreateAccount: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {

}
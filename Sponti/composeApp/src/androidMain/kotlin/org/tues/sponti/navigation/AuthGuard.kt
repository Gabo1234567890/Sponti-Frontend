package org.tues.sponti.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.navigation.NavController
import org.tues.sponti.data.auth.SessionManager

@Composable
fun AuthGuard(
    sessionManager: SessionManager,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val isLoggedIn by produceState<Boolean?>(null) {
        value = sessionManager.getAccessToken() != null
    }

    when (isLoggedIn) {
        null -> Unit
        false -> LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
        true -> content()
    }
}
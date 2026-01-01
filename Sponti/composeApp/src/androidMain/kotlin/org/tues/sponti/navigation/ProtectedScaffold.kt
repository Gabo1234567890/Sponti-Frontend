package org.tues.sponti.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.tues.sponti.SpontiApp
import org.tues.sponti.ui.screens.main.MainScaffold

@Composable
fun ProtectedScaffold(navController: NavHostController, content: @Composable (PaddingValues) -> Unit) {
    val sessionManager = (navController.context.applicationContext as SpontiApp).sessionManager

    AuthGuard(sessionManager = sessionManager, navController = navController) {
        MainScaffold(navController = navController, content = content)
    }
}
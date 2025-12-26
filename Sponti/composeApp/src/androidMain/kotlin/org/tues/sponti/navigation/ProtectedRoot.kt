package org.tues.sponti.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.tues.sponti.SpontiApp
import org.tues.sponti.ui.screens.main.MainScaffold

@Composable
fun ProtectedRoot(navController: NavHostController) {
    val sessionManager = (navController.context.applicationContext as SpontiApp).sessionManager

    AuthGuard(sessionManager = sessionManager, navController = navController) {
        MainScaffold(navController) {
            MainNavGraph(navController)
        }
    }
}
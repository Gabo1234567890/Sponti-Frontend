package org.tues.sponti.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.tues.sponti.ui.screens.createaccount.CreateAccountScreen
import org.tues.sponti.ui.screens.login.LogInScreen

@Composable
fun AppNavGraph(
    navController: NavHostController, startDestination: String = Routes.CREATE_ACCOUNT
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            Routes.CREATE_ACCOUNT
        ) {
            CreateAccountScreen(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onSuccess = { navController.navigate(Routes.VERIFY_EMAIL) })
        }

        composable(Routes.LOGIN) {
            LogInScreen(
                onNavigateToCreateAccount = { navController.navigate(Routes.CREATE_ACCOUNT) },
                onNavigateToForgotPassword = {},
                onSuccess = {}
            )
        }

        composable(Routes.VERIFY_EMAIL) { }
    }
}
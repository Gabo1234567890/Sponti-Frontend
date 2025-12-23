package org.tues.sponti.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.tues.sponti.deeplink.DeepLinkEvent
import org.tues.sponti.deeplink.DeepLinkManager
import org.tues.sponti.ui.screens.createaccount.CreateAccountScreen
import org.tues.sponti.ui.screens.forgotpassword.ForgotPasswordScreens
import org.tues.sponti.ui.screens.login.LogInScreen

@Composable
fun AppNavGraph(
    navController: NavHostController, startDestination: String = Routes.CREATE_ACCOUNT
) {
    val deepLinkEvent by DeepLinkManager.events.collectAsState()

    LaunchedEffect(deepLinkEvent) {
        when (val event = deepLinkEvent) {
            is DeepLinkEvent.ResetPassword -> {
                navController.navigate("${Routes.FORGOT_PASSWORD}?token=${event.token}&email=${event.email}")
                DeepLinkManager.consume()
            }

            null -> Unit
        }
    }

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
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onSuccess = {})
        }

        composable(Routes.VERIFY_EMAIL) { }

        composable(
            route = Routes.FORGOT_PASSWORD + "?token={token}&email={email}",
            arguments = listOf(navArgument("token") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }, navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "sponti://forgot-password?token={token}&email={email}"
                })) { backStackEntry ->
            ForgotPasswordScreens(
                onBackToLogin = { navController.navigate(Routes.LOGIN) },
                token = backStackEntry.arguments?.getString("token") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                backStackEntry = backStackEntry
            )
        }
    }
}
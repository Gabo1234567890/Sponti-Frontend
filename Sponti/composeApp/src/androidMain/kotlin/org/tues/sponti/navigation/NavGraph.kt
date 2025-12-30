package org.tues.sponti.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.tues.sponti.ui.screens.createaccount.CreateAccountScreen
import org.tues.sponti.ui.screens.forgotpassword.ForgotPasswordScreens
import org.tues.sponti.ui.screens.home.HomeScreen
import org.tues.sponti.ui.screens.login.LogInScreen
import org.tues.sponti.ui.screens.verifyemail.VerifyEmailScreens

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
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0)
                    }
                })
        }

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
                })
        ) { backStackEntry ->
            ForgotPasswordScreens(
                onBackToLogin = { navController.navigate(Routes.LOGIN) },
                token = backStackEntry.arguments?.getString("token") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                backStackEntry = backStackEntry
            )
        }

        composable(
            route = Routes.VERIFY_EMAIL + "?token={token}&email={email}",
            arguments = listOf(navArgument("token") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }, navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }),
            deepLinks = listOf(navDeepLink {
                uriPattern = "sponti://verify-email?token={token}&email={email}"
            })
        ) { backStackEntry ->
            VerifyEmailScreens(
                onBackToLogin = { navController.navigate(Routes.LOGIN) },
                backStackEntry = backStackEntry
            )
        }

        composable(route = Routes.HOME) {
            ProtectedScaffold(navController) {
                HomeScreen(navController)
            }
        }
    }
}
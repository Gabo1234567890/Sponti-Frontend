package org.tues.sponti.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.tues.sponti.ui.screens.challenge.ChallengeScreen
import org.tues.sponti.ui.screens.createaccount.CreateAccountScreen
import org.tues.sponti.ui.screens.createchallenge.CreateChallengeScreen
import org.tues.sponti.ui.screens.forgotpassword.ForgotPasswordScreens
import org.tues.sponti.ui.screens.home.HomeRoute
import org.tues.sponti.ui.screens.login.LogInScreen
import org.tues.sponti.ui.screens.profile.ProfileScreen
import org.tues.sponti.ui.screens.settings.SettingsScreen
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
            HomeRoute(navController = navController, main = true)
        }

        composable(route = Routes.ADD) {
            ProtectedScaffold(navController = navController, main = true) { paddingValues ->
                CreateChallengeScreen(
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }

        composable(route = Routes.PROFILE) {
            ProtectedScaffold(navController = navController, main = true) { paddingValues ->
                ProfileScreen(
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }

        composable(route = Routes.CHALLENGE + "/{id}", arguments = listOf(navArgument("id") {
            type = NavType.StringType
            nullable = false
        })) { backStackEntry ->
            ProtectedScaffold(navController = navController, main = false) { paddingValues ->
                ChallengeScreen(
                    challengeId = backStackEntry.arguments?.getString("id") ?: "",
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }

        composable(route = Routes.SETTINGS) {
            ProtectedScaffold(navController = navController, main = false) { paddingValues ->
                SettingsScreen(
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}
package org.tues.sponti.navigation

sealed class Screen(val route: String, val protected: Boolean) {
    object CreateAccount: Screen("create-account", false)
    object Login: Screen("login", false)
    object VerifyEmail: Screen("verify-email", false)
    object ForgotPassword: Screen("forgot-password", false)
}
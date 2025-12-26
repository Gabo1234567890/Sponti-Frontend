package org.tues.sponti.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Routes.HOME
    ) {
        composable(route = Routes.HOME) {}

        composable(route = Routes.ADD) {}

        composable(route = Routes.PROFILE) {}
    }
}
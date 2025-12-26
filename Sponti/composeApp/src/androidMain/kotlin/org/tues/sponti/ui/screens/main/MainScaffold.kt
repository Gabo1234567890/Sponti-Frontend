package org.tues.sponti.ui.screens.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.tues.sponti.navigation.Routes
import org.tues.sponti.ui.components.BottomBar
import org.tues.sponti.ui.components.PrimaryTopBar

@Composable
fun MainScaffold(navController: NavHostController, content: @Composable () -> Unit) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    Scaffold(topBar = {
        PrimaryTopBar(
            onSettings = { navController.navigate(Routes.SETTINGS) }, title = currentRoute
        )
    }, bottomBar = {
        BottomBar(
            currentRoute = currentRoute, onItemSelected = { item ->
                navController.navigate(item.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(Routes.HOME) {
                        saveState = true
                    }
                }
            })
    }) {
        content()
    }
}
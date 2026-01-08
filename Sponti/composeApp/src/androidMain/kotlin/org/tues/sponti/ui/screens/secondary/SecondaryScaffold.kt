package org.tues.sponti.ui.screens.secondary

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.tues.sponti.ui.components.SecondaryTopBar

@Composable
fun SecondaryScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    Scaffold(
        topBar = {
            SecondaryTopBar(
                onBack = { navController.popBackStack() },
                title = currentRoute.substringBefore("/").replaceFirstChar { it.uppercase() }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
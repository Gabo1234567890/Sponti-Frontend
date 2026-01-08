package org.tues.sponti.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.tues.sponti.SpontiApp
import org.tues.sponti.ui.screens.main.MainScaffold
import org.tues.sponti.ui.screens.secondary.SecondaryScaffold

@Composable
fun ProtectedScaffold(
    navController: NavHostController,
    main: Boolean,
    content: @Composable (PaddingValues) -> Unit,
) {
    val sessionManager = (navController.context.applicationContext as SpontiApp).sessionManager

    AuthGuard(sessionManager = sessionManager, navController = navController) {
        if (main) MainScaffold(navController = navController, content = content)
        else SecondaryScaffold(navController = navController, content = content)
    }
}
package org.tues.sponti.navigation

import androidx.annotation.DrawableRes
import org.tues.sponti.R

sealed class BottomBarItem(val route: String, @DrawableRes val icon: Int) {
    object Add: BottomBarItem(Routes.ADD, R.drawable.add)
    object Home: BottomBarItem(Routes.HOME, R.drawable.home)
    object Profile: BottomBarItem(Routes.PROFILE, R.drawable.person)
}
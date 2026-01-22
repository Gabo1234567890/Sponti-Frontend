package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.tues.sponti.navigation.BottomBarItem
import org.tues.sponti.ui.theme.Base0

@Composable
fun BottomBar(currentRoute: String, onItemSelected: (BottomBarItem) -> Unit) {
    val items = listOf(BottomBarItem.Add, BottomBarItem.Home, BottomBarItem.Profile)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = 30.dp,
                clip = false,
                spotColor = Color.Black,
            )
            .background(color = Color.Transparent)
            .padding(top = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(color = Base0)
        ) { }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                BottomBarItemView(
                    item = item,
                    selected = selected,
                    modifier = Modifier.zIndex(1f),
                    onClick = { onItemSelected(item) })
            }
        }
    }
}
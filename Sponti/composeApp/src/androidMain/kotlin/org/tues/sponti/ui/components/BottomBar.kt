package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.tues.sponti.navigation.BottomBarItem
import org.tues.sponti.ui.theme.Base0

@Composable
fun BottomBar(currentRoute: String, onItemSelected: (BottomBarItem) -> Unit) {
    val items = listOf(BottomBarItem.Add, BottomBarItem.Home, BottomBarItem.Profile)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                shadowElevation = 16.dp.toPx()
                clip = false
            }
            .background(color = Base0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                BottomBarItemView(
                    item = item,
                    selected = selected,
                    onClick = { onItemSelected(item) })
            }
        }
    }
}
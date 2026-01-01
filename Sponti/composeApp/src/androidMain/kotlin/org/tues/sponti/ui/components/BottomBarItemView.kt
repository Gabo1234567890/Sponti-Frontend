package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.navigation.BottomBarItem
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Primary1

@Composable
fun BottomBarItemView(item: BottomBarItem, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = if (selected) 8.dp else 20.dp,
                vertical = if (selected) 8.dp else 12.dp
            )
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(color = Primary1)
            ) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = item.route,
                    tint = Base0
                )
            }
        } else Icon(
            painter = painterResource(item.icon),
            contentDescription = item.route,
            tint = Primary1
        )
    }
}
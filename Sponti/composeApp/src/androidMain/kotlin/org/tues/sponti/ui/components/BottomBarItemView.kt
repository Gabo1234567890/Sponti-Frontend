package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.navigation.BottomBarItem
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Primary1

@Composable
fun BottomBarItemView(
    item: BottomBarItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 64.dp, height = if (selected) 64.dp else 48.dp)
            .offset(y = if (selected) (-12).dp else 0.dp)
            .background(Base0, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Primary1, CircleShape),
                contentAlignment = Alignment.Center
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
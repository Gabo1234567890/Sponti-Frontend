package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Heading8
import org.tues.sponti.ui.theme.Primary1

@Composable
fun FilterButton(
    iconId: Int, label: String, selected: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .graphicsLayer {
                shadowElevation = 16.dp.toPx()
                shape = RoundedCornerShape(16.dp)
                clip = false
            }
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Base0)
            .clickable(onClick = onClick)
            .zIndex(if (selected) 1f else 0f),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Icon(
                painter = painterResource(iconId),
                contentDescription = label,
                tint = Primary1,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label, style = Heading8, color = Primary1
            )
        }
    }
}
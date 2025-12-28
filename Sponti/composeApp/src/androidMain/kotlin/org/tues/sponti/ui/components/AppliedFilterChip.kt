package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Primary1

@Composable
fun AppliedFilterChip(
    iconId: Int, text: String, onRemove: () -> Unit
) {
    Box(modifier = Modifier
        .padding(all = 8.dp)
        .graphicsLayer {
            shadowElevation = 16.dp.toPx()
            shape = RoundedCornerShape(16.dp)
            clip = false
        }
        .clip(shape = RoundedCornerShape(16.dp))
        .background(color = Primary1))
    {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                tint = Base0,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = text,
                style = Caption1,
                color = Base0
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "Close",
                    tint = Base0
                )
            }
        }
    }
}
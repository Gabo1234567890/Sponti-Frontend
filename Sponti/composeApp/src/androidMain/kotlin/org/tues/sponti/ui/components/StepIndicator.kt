package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base60
import org.tues.sponti.ui.theme.Primary1

@Composable
fun StepIndicator(steps: Int, current: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
        repeat(steps) { index ->
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(if (index == current) 40.dp else 20.dp)
                    .background(
                        if (index == current) Primary1 else Base60,
                        if (index == current) RoundedCornerShape(12.dp) else CircleShape
                    )
            )
        }
    }
}
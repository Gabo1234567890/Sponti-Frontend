package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Primary1

@Composable
fun Switch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .width(52.dp)
            .background(
                color = if (checked) Primary1 else Base80,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(all = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = if (checked) 24.dp else 0.dp)
                .background(color = Base0, shape = CircleShape)
        )
    }
}
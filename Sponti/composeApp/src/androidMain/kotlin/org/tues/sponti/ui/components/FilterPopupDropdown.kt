package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.tues.sponti.ui.theme.Base0

@Composable
fun DropdownFilterPopup(
    iconId: Int,
    options: List<String>,
    onApply: (Set<String>) -> Unit
) {
    var selected by remember { mutableStateOf(setOf<String>()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .graphicsLayer {
                shadowElevation = 16.dp.toPx()
                shape = RoundedCornerShape(16.dp)
                clip = false
            }
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Base0)
            .zIndex(1f)
    ) {
        Column {

        }
    }
}
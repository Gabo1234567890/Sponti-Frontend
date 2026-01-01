package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base0

@Composable
fun DropdownFilterPopup(
    iconId: Int,
    label: String,
    options: List<String>,
    onApply: (Set<String>) -> Unit
) {
    var selected by remember { mutableStateOf(setOf<String>()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Base0)
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Column {
            IconDropdownInputField(
                iconId = iconId,
                label = label,
                options = options,
                selected = selected
            ) { selected = it }
            Spacer(Modifier.height(8.dp))
            PrimaryButton(
                text = "Search",
                size = ButtonSize.Small,
                state = if (selected.isEmpty()) ButtonState.Disabled else ButtonState.Active,
                modifier = Modifier.fillMaxWidth()
            ) {
                onApply(selected)
            }
        }
    }
}
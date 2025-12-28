package org.tues.sponti.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Success

@Composable
fun IconDropdownInputField(
    iconId: Int,
    label: String,
    options: List<String>,
    selected: Set<String>,
    onSelectedChange: (Set<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Base80), RoundedCornerShape(12.dp))
                .background(color = Base0)
                .clickable { expanded = !expanded }
                .padding(horizontal = 8.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    tint = Base100,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = if (selected.isEmpty()) label else selected.joinToString(", "),
                    style = Caption1,
                    color = Base100,
                    maxLines = 1
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Arrow",
                    tint = Base100,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(if (expanded) 90f else -90f),
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Base80), RoundedCornerShape(12.dp))
                    .background(color = Base0)
            ) {
                options.forEach { option ->
                    val isSelected = option in selected

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, Base80))
                            .background(Base0)
                            .clickable {
                                val newSelection =
                                    if (isSelected) selected - option else selected + option

                                onSelectedChange(newSelection)
                            }
                            .padding(horizontal = 8.dp, vertical = 12.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            style = Caption1,
                            color = Base100,
                            modifier = Modifier.weight(1f)
                        )

                        if(isSelected) {
                            Icon(
                                painter = painterResource(R.drawable.tick),
                                contentDescription = "Added",
                                tint = Success,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
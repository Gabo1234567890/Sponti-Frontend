package org.tues.sponti.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Caption1

@Composable
fun DropdownInputField(
    iconId: Int?,
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    val focusManager = LocalFocusManager.current

    Column {
        Box(
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    anchorSize = coordinates.size
                }
                .border(BorderStroke(1.dp, Base80), RoundedCornerShape(12.dp))
                .background(color = Base0)
                .clickable { expanded = !expanded }
                .padding(horizontal = 8.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconId != null) {
                    Icon(
                        painter = painterResource(iconId),
                        contentDescription = null,
                        tint = Base100,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(Modifier.width(12.dp))
                }

                Text(
                    text = selected, style = Caption1, color = Base100, maxLines = 1
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
        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus(force = true)
                },
                properties = PopupProperties(focusable = true, dismissOnClickOutside = true),
                offset = IntOffset(0, anchorSize.height + 8)
            ) {
                Column(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { anchorSize.width.toDp() })
                        .border(BorderStroke(1.dp, Base80), RoundedCornerShape(12.dp))
                        .background(color = Base0)
                ) {
                    options.forEachIndexed { index, option ->

                        if (index > 0) {
                            HorizontalDivider(thickness = 1.dp, color = Base80)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(Base0)
                                .clickable {
                                    onSelectedChange(option)
                                    expanded = false
                                    focusManager.clearFocus(force = true)
                                }
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                style = Caption1,
                                color = Base100,
                            )
                        }
                    }
                }
            }
        }
    }
}
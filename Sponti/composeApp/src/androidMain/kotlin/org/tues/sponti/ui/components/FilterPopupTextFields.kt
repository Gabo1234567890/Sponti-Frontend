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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.screens.common.formattedTimeToMinutes
import org.tues.sponti.ui.screens.common.minutesToFormattedTimeString
import org.tues.sponti.ui.theme.Base0

@Composable
fun TextFieldsFilterPopup(
    iconId: Int,
    minLabel: String,
    maxLabel: String,
    maxDigits: Int,
    onApply: (Int?, Int?) -> Unit
) {
    var min by remember { mutableStateOf("") }
    var max by remember { mutableStateOf("") }

    var minInputState by remember { mutableStateOf(InputState.Default) }
    var maxInputState by remember { mutableStateOf(InputState.Default) }

    val duration = minLabel.contains("duration")

    val minVal = (min.toIntOrNull() ?: if (duration) min.formattedTimeToMinutes() else null)
    val maxVal = (max.toIntOrNull() ?: if (duration) max.formattedTimeToMinutes() else null)

    val valid =
        (minVal != null || maxVal != null) && (minVal == null || maxVal == null || minVal <= maxVal)

    val durationOutOfBounds =
        duration && listOf(minVal, maxVal).any { it != null && (it < 0 || it > (24 * 60)) }

    val allowedChars = if (duration) "[^0-9:]" else "[^0-9]"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Base0)
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Column {
            IconInputField(
                iconId = iconId,
                value = min,
                onValueChange = {
                    if (!Regex(allowedChars).containsMatchIn(it)) {
                        min = it
                    }
                },
                placeholder = minLabel,
                inputState = minInputState,
                maxLength = maxDigits,
                onFocusChange = { focused ->
                    minInputState =
                        if (focused) InputState.Active else if (min.isEmpty()) InputState.Default else InputState.Filled
                    if (duration && minInputState == InputState.Filled) min =
                        if (min.toIntOrNull() == null) min else min.toInt()
                            .minutesToFormattedTimeString()
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            IconInputField(
                iconId = iconId,
                value = max,
                onValueChange = {
                    if (!Regex(allowedChars).containsMatchIn(it)) {
                        max = it
                    }
                },
                placeholder = maxLabel,
                inputState = maxInputState,
                maxLength = maxDigits,
                onFocusChange = { focused ->
                    maxInputState =
                        if (focused) InputState.Active else if (max.isEmpty()) InputState.Default else InputState.Filled
                    if (duration && maxInputState == InputState.Filled) max =
                        if (max.toIntOrNull() == null) max else max.toInt()
                            .minutesToFormattedTimeString()
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            PrimaryButton(
                text = "Search",
                size = ButtonSize.Small,
                state =
                    if (valid)
                        if (durationOutOfBounds) ButtonState.Disabled
                        else ButtonState.Active
                    else ButtonState.Disabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                onApply(minVal, maxVal)
            }
        }
    }
}
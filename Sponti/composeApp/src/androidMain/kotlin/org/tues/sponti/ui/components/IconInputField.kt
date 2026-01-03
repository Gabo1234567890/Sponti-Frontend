package org.tues.sponti.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Primary1

@Composable
fun IconInputField(
    iconId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    inputState: InputState,
    maxLength: Int,
    onFocusChange: (Boolean) -> Unit
) {
    val borderColor = when (inputState) {
        InputState.Default -> Base80
        InputState.Active -> Primary1
        InputState.Filled -> Base100
        else -> {
            Color.Unspecified
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = null,
            tint = Base100,
            modifier = Modifier.size(16.dp)
        )

        Spacer(Modifier.width(12.dp))

        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                textStyle = Caption1,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { onFocusChange.invoke(it.isFocused) },
                decorationBox = { innerTextFiled ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, style = Caption1, color = Base80)
                    }
                    innerTextFiled()
                },
                cursorBrush = SolidColor(Base100),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
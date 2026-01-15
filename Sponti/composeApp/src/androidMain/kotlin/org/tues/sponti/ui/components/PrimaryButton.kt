package org.tues.sponti.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Heading6
import org.tues.sponti.ui.theme.Heading7
import org.tues.sponti.ui.theme.Primary1

enum class ButtonSize { Large, Small }
enum class ButtonState { Active, Disabled }

@Composable
fun PrimaryButton(
    text: String,
    size: ButtonSize,
    state: ButtonState,
    modifier: Modifier = Modifier,
    color: Color? = null,
    onClick: () -> Unit
) {
    val backGroundColor = color ?: if (state == ButtonState.Active) Primary1 else Base80

    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .background(backGroundColor, shape)
            .then(if (state == ButtonState.Active) Modifier.clickable { onClick() } else Modifier)
            .then(
                if (size == ButtonSize.Large) Modifier
                    .fillMaxWidth()
                    .heightIn(max = 56.dp)
                    .padding(all = 16.dp) else Modifier
                    .heightIn(max = 40.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .widthIn(min = 152.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (size == ButtonSize.Large) Heading6 else Heading7,
            color = Base0
        )
    }
}
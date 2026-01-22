package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.screens.admin.AdminTabButtonPosition
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Heading6
import org.tues.sponti.ui.theme.Primary1

@Composable
fun AdminTabButton(
    text: String,
    selected: Boolean,
    position: AdminTabButtonPosition,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape =
        when (position) {
            AdminTabButtonPosition.LEFT ->
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                )

            AdminTabButtonPosition.MIDDLE -> RectangleShape

            AdminTabButtonPosition.RIGHT ->
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 20.dp
                )
        }

    Box(
        modifier = modifier
            .background(
                color = if (selected) Primary1 else Base0,
                shape = shape
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Heading6,
            color = if (selected) Base0 else Primary1
        )
    }
}
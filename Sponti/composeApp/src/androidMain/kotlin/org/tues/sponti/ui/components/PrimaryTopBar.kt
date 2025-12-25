package org.tues.sponti.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Primary1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(onSettings: () -> Unit, title: String) {
    TopAppBar(
        title = { Text(text = title, style = Heading3, color = Primary1) },
        navigationIcon = {
            IconButton(onClick = onSettings) {
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = "Settings",
                    tint = Primary1
                )
            }
        }
    )
}
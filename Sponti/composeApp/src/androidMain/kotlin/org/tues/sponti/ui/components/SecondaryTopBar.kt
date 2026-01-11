package org.tues.sponti.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Primary1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(onBack: () -> Unit, title: String) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = Heading3, color = Primary1) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Back",
                    tint = Primary1
                )
            }
        }
    )
}
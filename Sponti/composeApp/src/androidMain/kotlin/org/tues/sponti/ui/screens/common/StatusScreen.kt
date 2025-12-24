package org.tues.sponti.ui.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Error
import org.tues.sponti.ui.theme.Heading2
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Paragraph1
import org.tues.sponti.ui.theme.Primary1
import org.tues.sponti.ui.theme.Success

@Composable
fun StatusScreen(
    title: String,
    iconId: Int,
    headline: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fail = headline.lowercase().contains("failed")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Base0)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_name),
                contentDescription = "Sponti Logo"
            )
            Spacer(Modifier.height(12.dp))
            Text(text = title, style = Heading3, color = Primary1)
        }
        Spacer(Modifier.height(100.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = if (fail) "Fail" else "Success",
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(if (fail) Error else Success)
            )
            Spacer(Modifier.height(20.dp))
            Text(text = headline, style = Heading2, color = if (fail) Error else Success)
            Spacer(Modifier.height(8.dp))
            Text(text = description, style = Paragraph1, color = Base80)
        }
        Spacer(Modifier.height(100.dp))
        PrimaryButton(
            text = buttonText, size = ButtonSize.Large, state = ButtonState.Active
        ) {
            onClick()
        }
    }
}
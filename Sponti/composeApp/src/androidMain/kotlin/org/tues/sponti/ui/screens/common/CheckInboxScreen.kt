package org.tues.sponti.ui.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.ui.components.StepIndicator
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Heading3
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Primary1

@Composable
fun CheckInboxScreen(
    title: String,
    inboxMessage: String,
    modifier: Modifier = Modifier
) {
    val forgotPass = title.lowercase() == "forgot password"

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
        Text(text = inboxMessage, style = Heading4, color = Base100, textAlign = TextAlign.Center)
        if(forgotPass) {
            Spacer(Modifier.height(100.dp))
            StepIndicator(3, 1)
        }
    }
}
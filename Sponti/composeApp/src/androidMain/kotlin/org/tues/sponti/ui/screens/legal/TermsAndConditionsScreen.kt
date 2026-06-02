package org.tues.sponti.ui.screens.legal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Paragraph1

@Composable
fun TermsAndConditionsScreen(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        text = LegalTexts.TERMS,
        style = Paragraph1,
        color = Base100
    )
}
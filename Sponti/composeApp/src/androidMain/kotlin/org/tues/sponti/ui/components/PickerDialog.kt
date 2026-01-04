package org.tues.sponti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Heading4

@Composable
fun PickerDialog(onGallery: () -> Unit, onCamera: () -> Unit, onDismiss: () -> Unit) {
    val focusManager = LocalFocusManager.current

    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            focusManager.clearFocus(force = true)
            onDismiss()
        },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PrimaryButton(
                    text = "Camera",
                    size = ButtonSize.Large,
                    state = ButtonState.Active
                ) {
                    focusManager.clearFocus(force = true)
                    onCamera()
                }
                PrimaryButton(
                    text = "Gallery", size = ButtonSize.Large,
                    state = ButtonState.Active
                ) {
                    focusManager.clearFocus(force = true)
                    onGallery()
                }
            }
        },
        title = {
            Text(
                text = "Select image source",
                style = Heading4,
                color = Base100,
                textAlign = TextAlign.Center
            )
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}
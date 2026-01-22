package org.tues.sponti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Paragraph1

@Composable
fun PickerDialog(
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onDismiss: () -> Unit,
    descriptionId: Int
) {
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = {
            focusManager.clearFocus(force = true)
            onDismiss()
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        focusManager.clearFocus(force = true)
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cross),
                        contentDescription = "Close",
                        tint = Base100,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.pickerDialogTitle),
                    style = Heading4,
                    color = Base100,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(descriptionId),
                    style = Paragraph1,
                    color = Base80,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Spacer(Modifier.height(12.dp))
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
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}
package org.tues.sponti.ui.components

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
fun FinishConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { onDismiss() },
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
                    text = stringResource(R.string.finishConfirmationDialogTitle),
                    style = Heading4,
                    color = Base100,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.finishConfirmationDialogDescription),
                    style = Paragraph1,
                    color = Base80,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                text = "Continue",
                size = ButtonSize.Large,
                state = ButtonState.Active
            ) { onConfirm() }
        },
        dismissButton = {
            PrimaryButton(
                text = "Cancel",
                size = ButtonSize.Large,
                state = ButtonState.Active,
                color = Base80
            ) { onDismiss() }
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}
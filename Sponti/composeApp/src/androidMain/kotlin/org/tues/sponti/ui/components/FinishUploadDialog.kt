package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Paragraph1
import java.io.File

@Composable
fun FinishUploadDialog(
    images: List<File>,
    isSubmitting: Boolean,
    onAddImage: () -> Unit,
    onFinish: () -> Unit,
    onDismiss: () -> Unit,
    onRemove: (File) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
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

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = stringResource(R.string.finishUploadDialogTitle),
                        style = Heading4,
                        color = Base100,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.finishUploadDialogDescription),
                        style = Paragraph1,
                        color = Base80,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(images) {
                            Box {
                                IconButton(
                                    onClick = { onRemove(it) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(24.dp)
                                        .background(Base0, CircleShape)
                                        .padding(4.dp)
                                        .zIndex(1f)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.cross),
                                        contentDescription = "Remove image",
                                        tint = Base100,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                AsyncImage(
                                    model = it,
                                    contentDescription = "Uploaded image",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (images.size < 3) {
                    PrimaryButton(
                        text = "Add image",
                        size = ButtonSize.Large,
                        state = ButtonState.Active
                    ) { onAddImage() }
                }

                PrimaryButton(
                    text = if (isSubmitting) "Finishing..." else "Finish",
                    size = ButtonSize.Large,
                    state = ButtonState.Active
                ) { onFinish() }
            }
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}
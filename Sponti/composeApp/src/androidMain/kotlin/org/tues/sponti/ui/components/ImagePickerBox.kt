package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base60
import java.io.File

@Composable
fun ImagePickerBox(
    imageFile: File?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(144.dp)
            .background(
                color = Base60,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageFile == null) {
            Icon(
                painter = painterResource(R.drawable.add_image),
                contentDescription = "Add image",
                tint = Base0,
                modifier = Modifier.size(48.dp)
            )
        } else {
            AsyncImage(
                model = imageFile,
                contentDescription = "Added image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.tues.sponti.R
import org.tues.sponti.data.user.MemoryItemDto
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Caption2
import org.tues.sponti.ui.theme.Heading8

@Composable
fun MemoryCard(memory: MemoryItemDto) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Base0)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = memory.imageUrl,
                contentDescription = "Memory image",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.image_placeholder),
                error = painterResource(R.drawable.image_placeholder)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = memory.challengeTitle,
                    style = Heading8,
                    color = Base100
                )
                Text(
                    text = memory.uploadedAt.slice(indices = IntRange(0, 9)),
                    style = Caption2,
                    color = Base80
                )
            }
        }
    }
}
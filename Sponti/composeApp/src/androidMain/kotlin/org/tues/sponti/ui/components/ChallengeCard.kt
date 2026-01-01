package org.tues.sponti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.tues.sponti.R
import org.tues.sponti.ui.screens.common.ChallengeType
import org.tues.sponti.ui.screens.common.minutesToFormattedTimeString
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Heading6
import org.tues.sponti.ui.theme.Heading8
import org.tues.sponti.ui.theme.Primary1

@Composable
fun ChallengeCard(
    challenge: ChallengeType, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false,
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Base0)
            .padding(all = 16.dp)
            .clickable(onClick = onClick)) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = challenge.title, style = Heading6, color = Primary1
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = challenge.thumbnailUrl,
                    contentDescription = "Thumbnail",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.width(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = challenge.description,
                        style = Caption1,
                        color = Base100,
                        maxLines = 4,
                        overflow = TextOverflow.Clip
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = "Location",
                            tint = Primary1,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = challenge.place, style = Heading8, color = Primary1
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row {
                ChallengeDetailChip(R.drawable.price, challenge.price.toString())
                Spacer(Modifier.width(12.dp))
                ChallengeDetailChip(
                    R.drawable.time, challenge.duration.minutesToFormattedTimeString()
                )
                Spacer(Modifier.width(12.dp))
                ChallengeDetailChip(
                    R.drawable.vehicle,
                    challenge.vehicle.name.lowercase().replaceFirstChar { it.uppercase() }
                )
            }
        }
    }
}
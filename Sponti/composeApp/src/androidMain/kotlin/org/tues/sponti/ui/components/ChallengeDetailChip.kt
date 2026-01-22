package org.tues.sponti.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base20
import org.tues.sponti.ui.theme.Heading7
import org.tues.sponti.ui.theme.Heading8
import org.tues.sponti.ui.theme.Primary1

@Composable
fun ChallengeDetailChip(
    iconId: Int,
    text: String,
    small: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(BorderStroke(1.dp, Base20), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(color = Base0),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = null,
            tint = Primary1,
            modifier = Modifier.size(if (small) 16.dp else 20.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = if (small) Heading8 else Heading7,
            color = Primary1
        )
    }

}
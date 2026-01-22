package org.tues.sponti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Heading6

@Composable
fun PaginationBar(page: Int, totalPages: Int, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev, enabled = page > 1) {
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Previous",
                tint = Base100
            )
        }

        Text(
            text = "Page $page / $totalPages",
            style = Heading6,
            color = Base100
        )

        IconButton(onClick = onNext, enabled = page < totalPages) {
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Next",
                tint = Base100,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}
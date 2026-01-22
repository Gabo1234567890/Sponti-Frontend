package org.tues.sponti.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.data.network.AdminChallengeListItem
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Error
import org.tues.sponti.ui.theme.Heading7
import org.tues.sponti.ui.theme.Primary1
import org.tues.sponti.ui.theme.Success

@Composable
fun ChallengesTable(
    challenges: List<AdminChallengeListItem>,
    onApprove: ((String) -> Unit)? = null,
    onDelete: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val approved = onApprove == null

    LazyColumn(
        modifier = Modifier.border(
            border = BorderStroke(width = 1.dp, color = Base80),
            shape = RoundedCornerShape(20.dp)
        )
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Title",
                        style = Heading7,
                        color = Base100,
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                if (!approved) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Approve",
                            style = Heading7,
                            color = Base100,
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = Base80
                    )
                }
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (approved) "Delete" else "Deny",
                        style = Heading7,
                        color = Base100,
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Go",
                        style = Heading7,
                        color = Base100,
                    )
                }
            }
        }
        items(challenges) { chal ->
            HorizontalDivider(thickness = 1.dp, color = Base80)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chal.title ?: "-",
                        style = Heading7,
                        color = Base100,
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                if (!approved) {
                    IconButton(
                        enabled = chal.id != null,
                        onClick = { onApprove(chal.id!!) },
                        modifier = Modifier.width(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.tick),
                            contentDescription = "Approve",
                            tint = Success,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = Base80
                    )
                }
                IconButton(
                    enabled = chal.id != null,
                    onClick = { onDelete(chal.id!!) },
                    modifier = Modifier.width(48.dp)
                ) {
                    Icon(
                        painter = painterResource(if (approved) R.drawable.trash else R.drawable.deny),
                        contentDescription = "Delete",
                        tint = Error,
                        modifier = Modifier.size(20.dp)
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                IconButton(
                    enabled = chal.id != null,
                    onClick = { onNavigate(chal.id!!) },
                    modifier = Modifier.width(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.forward),
                        contentDescription = "Navigate",
                        tint = Primary1,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
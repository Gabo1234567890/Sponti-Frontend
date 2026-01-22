package org.tues.sponti.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.tues.sponti.data.network.AdminUserListItem
import org.tues.sponti.data.user.Role
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Error
import org.tues.sponti.ui.theme.Heading7
import org.tues.sponti.ui.theme.Heading8

@Composable
fun UsersTable(
    users: List<AdminUserListItem>,
    onRoleChange: (String, Role) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .border(
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
                        .weight(1.5f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Username/Email",
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
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Role",
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
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Created at",
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
                        text = "Delete",
                        style = Heading7,
                        color = Base100,
                    )
                }
            }
        }

        items(users) { user ->
            HorizontalDivider(thickness = 1.dp, color = Base80)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = user.username ?: "-",
                        style = Heading7,
                        color = Base100,
                    )
                    Text(
                        text = user.email ?: "-",
                        style = Heading8,
                        color = Base80,
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RoleDropdown(
                        currentRole = user.role,
                    ) { onRoleChange(user.id!!, it) }
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.createdAt?.slice(IntRange(0, 9)) ?: "-",
                        style = Heading7,
                        color = Base100,
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Base80
                )
                IconButton(
                    enabled = user.id != null,
                    onClick = { onDelete(user.id!!) },
                    modifier = Modifier.width(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trash),
                        contentDescription = "Delete",
                        tint = Error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
package org.tues.sponti.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.sponti.R
import org.tues.sponti.data.user.Role
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Heading7

@Composable
fun RoleDropdown(currentRole: Role?, modifier: Modifier = Modifier, onSelect: (Role) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row (
            modifier = modifier.clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentRole?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "-",
                style = Heading7,
                color = Base100
            )
            Spacer(Modifier
                .width(4.dp)
                .height(4.dp))
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Arrow",
                modifier = Modifier
                    .size(16.dp)
                    .rotate(if (expanded) 90f else -90f),
                tint = Base100
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Role.entries.forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = Heading7,
                            color = Base100
                        )
                    },
                    onClick = {
                        expanded = false
                        if (role != currentRole) onSelect(role)
                    }
                )
            }
        }
    }
}
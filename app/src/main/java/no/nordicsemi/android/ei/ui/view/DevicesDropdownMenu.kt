package no.nordicsemi.android.ei.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeveloperBoard
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.ei.model.Device

@Composable
fun DevicesDropdownMenu(
    modifier: Modifier,
    connectedDevices: List<Device>,
    expanded: Boolean,
    onDeviceSelected: (Device) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss,
        content = {
            connectedDevices.forEach { device ->
                DropdownMenuItem(
                    leadingIcon = {
                        Image(
                            imageVector = Icons.Rounded.DeveloperBoard,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.Green,
                                    shape = CircleShape
                                )
                                .padding(8.dp),
                            colorFilter = ColorFilter.tint(Color.White),
                        )
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = device.name,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = device.deviceType,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    onClick = { onDeviceSelected(device) }
                )
            }
        }
    )
}
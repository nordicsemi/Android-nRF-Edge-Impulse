package no.nordicsemi.android.ei.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.ei.R
import no.nordicsemi.android.ei.service.param.MissingKeyException
import no.nordicsemi.android.ei.ui.layouts.AlertDialog
import no.nordicsemi.android.ei.ui.theme.NordicFall


@Composable
fun CreateKeyDialog(
    type: MissingKeyException.Type,
    onCreateKey: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        imageVector = Icons.Outlined.Key,
        title = stringResource(id = R.string.dialog_title_create_key, type.toString()),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(NordicFall),
                    )
                    Text(
                        text = stringResource(R.string.label_key_info),
                        color = NordicFall
                    )
                }

                OutlinedTextField(
                    enabled = false,
                    value = stringResource(R.string.label_key_name_default),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.label_key_name)) },
                )

                OutlinedTextField(
                    enabled = false,
                    value = stringResource(R.string.label_key_role_admin),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.label_key_role)) },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.label_key_dev),
                    )
                    Checkbox(
                        checked = true,
                        enabled = false,
                        onCheckedChange = {},
                    )
                }
            }
        },
        dismissText = stringResource(id = R.string.action_cancel),
        onDismiss = onDismiss,
        confirmText = stringResource(id = R.string.action_create),
        onConfirm = onCreateKey,
    )
}
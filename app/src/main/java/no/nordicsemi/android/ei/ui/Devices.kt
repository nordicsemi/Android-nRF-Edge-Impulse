/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package no.nordicsemi.android.ei.ui

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.DeveloperBoard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nordicsemi.android.ei.R
import no.nordicsemi.android.ei.ble.DiscoveredBluetoothDevice
import no.nordicsemi.android.ei.ble.rssiAsPercent
import no.nordicsemi.android.ei.ble.state.ScannerState
import no.nordicsemi.android.ei.ble.state.ScanningState
import no.nordicsemi.android.ei.comms.CommsManager
import no.nordicsemi.android.ei.model.Device
import no.nordicsemi.android.ei.ui.layouts.BluetoothDisabledInfo
import no.nordicsemi.android.ei.ui.layouts.BottomSheetAppBar
import no.nordicsemi.android.ei.ui.layouts.LocationTurnedOffInfo
import no.nordicsemi.android.ei.ui.layouts.NoConfiguredDevicesInfo
import no.nordicsemi.android.ei.ui.layouts.NoDevicesInRangeInfo
import no.nordicsemi.android.ei.ui.layouts.PermissionDeniedContent
import no.nordicsemi.android.ei.ui.layouts.PermissionNotGrantedContent
import no.nordicsemi.android.ei.util.Utils
import no.nordicsemi.android.ei.viewmodels.DevicesViewModel
import no.nordicsemi.android.ei.viewmodels.state.DeviceState
import no.nordicsemi.android.ei.viewmodels.state.indicatorColor

@Composable
fun Devices(
    scope: CoroutineScope,
    viewModel: DevicesViewModel,
    configuredDevices: List<Device>,
    activeDevices: Map<String, CommsManager>,
    refreshingState: Boolean,
    onRefresh: () -> Unit,
    scannerState: ScannerState,
    onBluetoothStateChanged: (Boolean) -> Unit,
    connect: (DiscoveredBluetoothDevice) -> Unit,
    dataAcquisitionTarget: (Device) -> Unit,
    disconnect: (DiscoveredBluetoothDevice) -> Unit,
    onRenameClick: (Device, String) -> Unit,
    onDeleteClick: (Device) -> Unit
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scanningState = scannerState.scanningState
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    BackHandler(
        enabled = bottomSheetState.isVisible,
        onBack = { scope.launch { bottomSheetState.hide() } }
    )
    PullToRefreshBox(
        state = rememberPullToRefreshState(),
        isRefreshing = refreshingState,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 160.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.label_devices),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            configuredDevices.takeIf { devices ->
                devices.isNotEmpty()
            }?.let { configuredDevices ->
                items(
                    items = configuredDevices,
                    key = { device -> device.deviceId }
                ) { configuredDevice ->
                    ConfiguredDeviceRow(
                        device = configuredDevice,
                        state = viewModel.deviceState(
                            configuredDevice = configuredDevice, activeDevices = activeDevices
                        ),
                        onDeviceClicked = { device ->
                            viewModel.onDeviceSelected(device)
                            showBottomSheet = !showBottomSheet
                        }
                    )
                }
            } ?: item {
                NoConfiguredDevicesInfo(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1.0f),
                        text = stringResource(R.string.label_scanner),
                        style = MaterialTheme.typography.labelLarge
                    )
                    if (scanningState == ScanningState.Started) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            }

            item {
                when {
                    Utils.isSorAbove() -> BluetoothPermissionsRequired(
                        scannerState = scannerState,
                        onBluetoothStateChanged = onBluetoothStateChanged
                    )

                    Utils.isBetweenMarshmallowAndS() -> LocationPermissionRequired(
                        scannerState = scannerState,
                        onBluetoothStateChanged = onBluetoothStateChanged
                    )

                    else -> BluetoothRequired(
                        scannerState = scannerState,
                        onBluetoothStateChanged = onBluetoothStateChanged
                    )
                }
            }
            if (scannerState.scanningState is ScanningState.Started) {
                scannerState.discoveredDevices
                    // Filter only devices that have not been configured.
                    .filter { discoveredDevice ->
                        configuredDevices.find { configuredDevice ->
                            configuredDevice.deviceId == discoveredDevice.bluetoothDevice.address
                        } == null
                    }
                    // Display only if at least one was found.
                    .takeIf { it.isNotEmpty() }
                    ?.let { discoveredDevices ->
                        this@LazyColumn.items(
                            items = discoveredDevices,
                            key = { it.bluetoothDevice.address }
                        ) { discoveredDevice ->
                            DiscoveredDeviceRow(
                                device = discoveredDevice,
                                state = activeDevices[discoveredDevice.deviceId]
                                    ?.connectivityState ?: DeviceState.IN_RANGE,
                                onDeviceClicked = { connect(it) },
                                onDeviceAuthenticated = { onRefresh() }
                            )
                        }
                    }
                // Else, show a placeholder.
                    ?: this@LazyColumn.item {
                        NoDevicesInRangeInfo(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }

        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            BottomSheetAppBar(
                imageVector = Icons.Default.Close,
                title = stringResource(id = R.string.label_device_information),
                onBackPressed = {
                    hideBottomSheet(scope = scope, bottomSheetState = bottomSheetState) {
                        showBottomSheet = false
                    }
                }
            )
            viewModel.device?.let { device ->
                DeviceDetails(
                    device = device,
                    deviceState = viewModel.deviceState(
                        configuredDevice = device,
                        activeDevices = activeDevices
                    ),
                    onConnectClick = {
                        viewModel.discoveredBluetoothDevice(device)?.let {
                            connect(it)
                            dataAcquisitionTarget(device)
                        }
                    },
                    onDisconnectClick = {
                        viewModel.discoveredBluetoothDevice(device)?.let(disconnect)
                    },
                    onRenameClick = onRenameClick
                ) { dev ->
                    onDeleteClick(dev)
                    hideBottomSheet(scope, bottomSheetState) {
                        showBottomSheet = false
                    }
                }
            }
        }
    }
}

internal fun hideBottomSheet(
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    onBottomSheetHide: () -> Unit
) {
    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
        if (!bottomSheetState.isVisible) {
            onBottomSheetHide()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun BluetoothPermissionsRequired(
    modifier: Modifier = Modifier,
    scannerState: ScannerState,
    onBluetoothStateChanged: (Boolean) -> Unit
) {
    val bluetoothPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT
        )
    )

    when {
        bluetoothPermissionsState.allPermissionsGranted -> {
            LocationRequired(
                modifier = modifier,
                scannerState = scannerState,
                onBluetoothStateChanged = onBluetoothStateChanged
            )
        }

        bluetoothPermissionsState.shouldShowRationale -> {
            PermissionDeniedContent(
                modifier = modifier,
                title = stringResource(id = R.string.bluetooth_scan_connect_permission_denied_title),
                text = stringResource(id = R.string.bluetooth_scan_connect_permission_denied_info)
            )
        }

        else -> {
            PermissionNotGrantedContent(
                modifier = modifier,
                title = stringResource(id = R.string.bluetooth_scan_connect_permission_required_title),
                text = stringResource(id = R.string.bluetooth_scan_connect_permission_info),
                onRequestPermission = { bluetoothPermissionsState.launchMultiplePermissionRequest() }
            )
        }
    }
}

@Composable
private fun BluetoothRequired(
    modifier: Modifier = Modifier,
    scannerState: ScannerState,
    onBluetoothStateChanged: (Boolean) -> Unit
) {
    val localContext = LocalContext.current
    var isBluetoothEnabled by remember { mutableStateOf(Utils.isBluetoothEnabled(context = localContext)) }
    SystemBroadcastReceiver(
        systemAction = BluetoothAdapter.ACTION_STATE_CHANGED
    ) { intent ->
        val currentState = intent?.getIntExtra(
            BluetoothAdapter.EXTRA_STATE,
            BluetoothAdapter.STATE_OFF
        )
        val previousState = intent?.getIntExtra(
            BluetoothAdapter.EXTRA_PREVIOUS_STATE,
            BluetoothAdapter.STATE_OFF
        )
        when (currentState) {
            BluetoothAdapter.STATE_ON -> {
                isBluetoothEnabled = true
                onBluetoothStateChanged(isBluetoothEnabled)
            }

            BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_OFF -> {
                if (previousState != BluetoothAdapter.STATE_TURNING_OFF &&
                    previousState != BluetoothAdapter.STATE_OFF
                ) {
                    isBluetoothEnabled = false
                    onBluetoothStateChanged(isBluetoothEnabled)
                }
            }
        }
    }

    if (isBluetoothEnabled) {
        onBluetoothStateChanged(isBluetoothEnabled)
    } else {
        BluetoothDisabledInfo(modifier = modifier, onBluetoothEnabled = {
            isBluetoothEnabled = true
        })
        scannerState.onBluetoothDisabled()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionRequired(
    modifier: Modifier = Modifier,
    scannerState: ScannerState,
    onBluetoothStateChanged: (Boolean) -> Unit
) {
    val permissionState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    when {
        permissionState.status.isGranted -> {
            LocationRequired(
                modifier = modifier,
                scannerState = scannerState,
                onBluetoothStateChanged = onBluetoothStateChanged
            )
        }

        permissionState.status.shouldShowRationale -> {
            PermissionDeniedContent(
                modifier = modifier,
                title = stringResource(id = R.string.location_permission_denied_title),
                text = stringResource(id = R.string.location_permission_denied_info)
            )
        }

        else -> {
            PermissionNotGrantedContent(
                modifier = modifier,
                title = stringResource(id = R.string.location_permission_title),
                text = stringResource(id = R.string.location_permission_info),
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        }
    }
}

@Composable
private fun LocationRequired(
    modifier: Modifier = Modifier,
    scannerState: ScannerState,
    onBluetoothStateChanged: (Boolean) -> Unit
) {
    val localContext = LocalContext.current
    var isLocationEnabled by remember { mutableStateOf(Utils.isLocationEnabled(context = localContext)) }
    SystemBroadcastReceiver(
        systemAction = LocationManager.MODE_CHANGED_ACTION
    ) {
        isLocationEnabled = Utils.isLocationEnabled(context = localContext)
    }
    if (!isLocationEnabled) {
        scannerState.onLocationTurnedOff()
        LocationTurnedOffInfo(modifier = modifier)
    } else {
        BluetoothRequired(
            modifier = modifier,
            scannerState = scannerState,
            onBluetoothStateChanged = onBluetoothStateChanged
        )
    }
}

@Composable
private fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (Intent?) -> Unit
) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(systemAction)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, currentOnSystemEvent) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context1: Context?, intent: Intent?) {
                onSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
fun ConfiguredDeviceRow(
    device: Device,
    state: DeviceState,
    onDeviceClicked: (Device) -> Unit
) {
    OutlinedCard(
        modifier = Modifier.clickable { onDeviceClicked(device) },
    ) {
        ListItem(
            modifier = Modifier.padding(vertical = 4.dp),
            leadingContent = {
                Image(
                    imageVector = Icons.Rounded.DeveloperBoard,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = state.indicatorColor(),
                            shape = CircleShape
                        )
                        .padding(8.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            },
            headlineContent = {
                Text(
                    text = device.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(text = device.deviceType)
            },
            trailingContent = {
                if (state == DeviceState.CONNECTING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun DiscoveredDeviceRow(
    device: DiscoveredBluetoothDevice,
    state: DeviceState,
    onDeviceClicked: (DiscoveredBluetoothDevice) -> Unit,
    onDeviceAuthenticated: () -> Unit,
) {
    OutlinedCard(
        modifier = Modifier
            .clickable(
                enabled = state == DeviceState.IN_RANGE,
                onClick = { onDeviceClicked(device) }
            ),
    ) {
        ListItem(
            modifier = Modifier.padding(vertical = 4.dp),
            leadingContent = {
                Image(
                    imageVector = Icons.Rounded.DeveloperBoard,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = state.indicatorColor(),
                            shape = CircleShape
                        )
                        .padding(8.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            },
            headlineContent = {
                Text(text = device.name ?: stringResource(id = R.string.unknown))
            },
            supportingContent = {
                Text(text = device.bluetoothDevice.address)
            },
            trailingContent = {
                when (state) {
                    // RSSI image can be displayed even when not in range
                    DeviceState.IN_RANGE, DeviceState.NOT_IN_RANGE -> {
                        Icon(
                            painter = painterResource(id = getRssiRes(device.rssiAsPercent())),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    DeviceState.CONNECTING,
                    DeviceState.AUTHENTICATING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    DeviceState.AUTHENTICATED -> {
                        // Once the device is authenticated we should refresh the list of devices.
                        onDeviceAuthenticated()
                    }
                }
            }
        )
    }
}

@DrawableRes
private fun getRssiRes(rssi: Int): Int = when (rssi) {
    in 0..20 -> R.drawable.ic_signal_0_bar
    in 21..40 -> R.drawable.ic_signal_1_bar
    in 41..60 -> R.drawable.ic_signal_2_bar
    in 61..80 -> R.drawable.ic_signal_3_bar
    else -> R.drawable.ic_signal_4_bar
}
/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Set the current name for a device.
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/devices/rename-device) for details.
 *
 * @param name  Device name
 */
data class RenameDeviceRequest(
    val name: String
)

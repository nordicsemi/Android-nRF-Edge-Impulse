/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Response for RenameDeviceRequest
 *
 * See [Documentation](https://docs.edgeimpulse.com/reference#renamedevice) for details.
 *
 * @param success   Whether the option succeeded.
 * @param error     Optional error description (set if 'success' was false).
 */
data class RenameDeviceResponse(
    val success: Boolean,
    val error: String?,
)

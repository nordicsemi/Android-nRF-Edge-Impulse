/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Response for RenameDeviceRequest
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/devices/rename-device) for details.
 */
data class RenameDeviceResponse(
    override val success: Boolean,
    override val error: String?,
): BaseResponse

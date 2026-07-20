/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Delete device response.
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/devices/delete-device) for details.
 */
data class DeleteDeviceResponse(
    override val success: Boolean = false,
    override val error: String?
): BaseResponse

/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Start sampling response.
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/devices/start-sampling#start-sampling) for details.
 *
 * @param success   Whether the option succeeded.
 * @param error     Optional error description (set if 'success' was false).
 * @param id        Sampling ID.
 */
data class StartSamplingResponse(
    val success: Boolean,
    val error: String?,
    val id: Number?
)

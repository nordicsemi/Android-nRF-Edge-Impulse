/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Response for "Add HMAC Key" request.
 *
 * @see <a href="https://docs.edgeimpulse.com/apis/studio/projects/add-hmac-key</a>
 */
data class AddHMACKeyResponse(
    override val success: Boolean,
    override val error: String?,
    val id: Number?,
): BaseResponse
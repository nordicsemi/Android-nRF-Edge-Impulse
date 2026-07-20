/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Response for "Add API Key" request.
 *
 * @see <a href="https://docs.edgeimpulse.com/apis/studio/projects/add-api-key</a>
 */
data class AddApiKeyResponse(
    override val success: Boolean,
    override val error: String?,
    val id: Number?,
    val apiKey: String,
): BaseResponse
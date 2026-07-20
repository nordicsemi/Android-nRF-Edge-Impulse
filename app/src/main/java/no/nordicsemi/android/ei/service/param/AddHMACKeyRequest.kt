/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Body parameters for "Add HMAC Key" request.
 *
 * @see <a href="https://docs.edgeimpulse.com/apis/studio/projects/add-hmac-key</a>
 */
data class AddHMACKeyRequest(
    /** Description of the key. */
    val name: String,
    /** Whether this key should be used as a development key. */
    val isDevelopmentKey: Boolean,
    /**
     * HMAC key.
     */
    val hmacKey: String
)

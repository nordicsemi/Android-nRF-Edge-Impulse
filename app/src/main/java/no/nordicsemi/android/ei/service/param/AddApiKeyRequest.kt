/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Body parameters for "Add API Key" request.
 *
 * @see <a href="https://docs.edgeimpulse.com/apis/studio/projects/add-api-key</a>
 */
data class AddApiKeyRequest(
    /** Description of the key. */
    val name: String,
    /** Whether this key should be used as a development key. */
    val isDevelopmentKey: Boolean,
    /**
     * Available options:
     * * `admin`,
     * * `readonly`,
     * * `ingestiononly`,
     * * `ingestion_deployment`
     */
    val role: String,
    /**
     * Optional: API key.
     *
     * This needs to start with ei_ and will need to be at least 32 characters long.
     * If this field is not passed in, a new API key is generated for you.
     */
    val apiKey: String? = null
)

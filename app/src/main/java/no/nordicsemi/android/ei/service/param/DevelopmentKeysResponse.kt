/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

import no.nordicsemi.android.ei.model.DevelopmentKeys
import no.nordicsemi.android.ei.model.Project

/**
 * Response body for a get Development Keys request.
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/projects/get-development-keys) for details.
 */
data class DevelopmentKeysResponse(
    override val success: Boolean,
    override val error: String?,
    val apiKey: String?,
    val hmacKey: String?,
): BaseResponse

fun DevelopmentKeysResponse.developmentKeys(project: Project) = DevelopmentKeys(
    apiKey = requireNotNull(apiKey) { throw MissingKeyException(project, MissingKeyException.Type.API_KEY) },
    hmacKey = requireNotNull(hmacKey) { throw MissingKeyException(project, MissingKeyException.Type.HMAC_KEY) },
)

class MissingKeyException(
    val project: Project,
    val type: Type
) : Exception() {

    enum class Type {
        API_KEY,
        HMAC_KEY;

        override fun toString() = when (this) {
            API_KEY -> "API key"
            HMAC_KEY -> "HMAC key"
        }
    }
}
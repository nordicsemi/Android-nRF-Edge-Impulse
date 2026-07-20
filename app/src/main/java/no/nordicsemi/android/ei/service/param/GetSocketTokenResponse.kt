/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

import no.nordicsemi.android.ei.model.SocketToken

/**
 * Response to GetSocketToken request.
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/projects/get-socket-token) for details.
 */
class GetSocketTokenResponse(
    override val success: Boolean,
    override val error: String?,
    val token: SocketToken
): BaseResponse
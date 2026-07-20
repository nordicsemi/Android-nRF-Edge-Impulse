/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Response for "Get JWT token" request.
 *
 * @see <a href="https://docs.edgeimpulse.com/apis/studio/login/get-jwt-token">Docs: Get JWT token</a>
 */
data class LoginResponse(
    override val success: Boolean,
    override val error: String?,
    /** JWT token, to be used to log in in the future through JWTAuthentication. */
    val token: String?,
): BaseResponse

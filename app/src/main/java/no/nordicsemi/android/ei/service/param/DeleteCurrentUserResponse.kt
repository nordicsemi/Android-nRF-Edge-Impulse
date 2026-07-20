/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Body parameters for "Delete current user" request.
 *
 * @see <a href="https://docs.edgeimpulse.com/apis/studio/user/delete-current-user">Docs: Delete current user</a>
 */
data class DeleteCurrentUserResponse(
    override val success: Boolean,
    override val error: String?
): BaseResponse

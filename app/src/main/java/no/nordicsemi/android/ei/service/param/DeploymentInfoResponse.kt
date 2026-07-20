/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.service.param

/**
 * Response to get deployment info.
 *
 * See [Documentation](https://docs.edgeimpulse.com/apis/studio/deployment/get-deployment-info) for details.
 */
data class DeploymentInfoResponse(
    override val success: Boolean,
    override val error: String?,
    val hasDeployment: Boolean,
    val version: Int
): BaseResponse
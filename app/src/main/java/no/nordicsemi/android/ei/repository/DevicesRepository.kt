/*
 * Copyright (c) 2026, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import no.nordicsemi.android.ei.di.IODispatcher
import no.nordicsemi.android.ei.service.EiService
import no.nordicsemi.android.ei.service.param.RenameDeviceRequest
import javax.inject.Inject

class DevicesRepository @Inject constructor(
    private val service: EiService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * List all devices for this project.
     *
     * Devices get included here if they connect to the remote management API or
     * if they have sent data to the ingestion API and had the `device_id` field set.
     */
    suspend fun listDevices(token: String, projectId: Int) =
        withContext(ioDispatcher) {
            service.listDevices(jwt = token, projectId = projectId)
        }

    /**
     * Set the current name for a device.
     *
     * @param name New name for this device.
     */
    suspend fun renameDevice(
        token: String,
        projectId: Int,
        deviceId: String,
        name: String
    ) = withContext(ioDispatcher) {
        service.renameDevice(
            jwt = token,
            projectId = projectId,
            /*Encode thr url manually as retrofit does not correctly encode https://github.com/square/retrofit/issues/3080*/
            deviceId = deviceId.replace(":", "%3A"),
            renameDeviceRequest = RenameDeviceRequest(name = name)
        )
    }

    /**
     * Delete a device.
     *
     * When this device sends a new message to ingestion or connects to remote
     * management the device will be recreated.
     */
    suspend fun deleteDevice(
        token: String,
        projectId: Int,
        deviceId: String
    ) = withContext(ioDispatcher) {
        service.deleteDevice(
            jwt = token,
            projectId = projectId,
            /*Encode thr url manually as retrofit does not correctly encode https://github.com/square/retrofit/issues/3080*/
            deviceId = deviceId.replace(":", "%3A")
        )
    }
}
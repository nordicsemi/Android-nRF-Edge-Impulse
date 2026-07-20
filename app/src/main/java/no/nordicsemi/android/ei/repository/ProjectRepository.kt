/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import no.nordicsemi.android.ei.di.IODispatcher
import no.nordicsemi.android.ei.model.Category
import no.nordicsemi.android.ei.model.DevelopmentKeys
import no.nordicsemi.android.ei.service.EiService
import no.nordicsemi.android.ei.service.param.BuildOnDeviceModelRequest
import no.nordicsemi.android.ei.service.param.RenameDeviceRequest
import no.nordicsemi.android.ei.service.param.StartSamplingRequest
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val service: EiService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * Retrieve all raw data by category.
     *
     * @param category Available options: `training`, `testing`, `validation`, `post-processing`, `all`.
     * @param offset Offset in results, can be used in conjunction with LimitResultsParameter to implement paging.
     * @param limit Maximum number of results to return.
     */
    suspend fun listSamples(
        keys: DevelopmentKeys,
        projectId: Int,
        category: String,
        offset: Int,
        limit: Int
    ) = withContext(ioDispatcher) {
        service.listSamples(
            apiKey = keys.apiKey,
            projectId = projectId,
            category = category,
            offset = offset,
            limit = limit
        )
    }

    /**
     * Start sampling on a device.
     *
     * This function returns immediately.
     * Updates are streamed through the websocket API.
     */
    suspend fun startSampling(
        keys: DevelopmentKeys,
        projectId: Int,
        deviceId: String,
        label: String,
        lengthMs: Number,
        category: Category,
        intervalMs: Float,
        sensor: String,
    ) = withContext(ioDispatcher) {
        service.startSampling(
            apiKey = keys.apiKey,
            projectId = projectId,
            deviceId = deviceId,
            startSamplingRequest = StartSamplingRequest(
                label = label,
                lengthMs = lengthMs,
                category = category.type,
                intervalMs = intervalMs,
                sensor = sensor
            )
        )
    }

    /**
     * Generate code to run the impulse on an embedded device.
     */
    suspend fun buildOnDeviceModels(
        keys: DevelopmentKeys,
        projectId: Int,
        type: String = "nordic-thingy53" // nordic-thingy53-nrf7002eb
    ) = withContext(ioDispatcher) {
        service.buildOnDevice(
            apiKey = keys.apiKey,
            projectId = projectId,
            type = type,
            buildOnDeviceModels = BuildOnDeviceModelRequest()
        )
    }

    /**
     * Gives information on whether a deployment was already built for a type.
     */
    suspend fun deploymentInfo(
        keys: DevelopmentKeys,
        projectId: Int,
        type: String = "nordic-thingy53" // nordic-thingy53-nrf7002eb
    ) = withContext(ioDispatcher) {
        service.deploymentInfo(
            apiKey = keys.apiKey,
            projectId = projectId,
            type = type,
        )
    }

    /**
     * Downloads latest build for the project.
     */
    suspend fun downloadBuild(
        keys: DevelopmentKeys,
        projectId: Int,
        type: String = "nordic-thingy53" // nordic-thingy53-nrf7002eb
    ) = withContext(ioDispatcher) {
        service.downloadBuild(
            apiKey = keys.apiKey,
            projectId = projectId,
            type = type,
        )
    }

    /**
     * List all devices for this project.
     *
     * Devices get included here if they connect to the remote management API or
     * if they have sent data to the ingestion API and had the `device_id` field set.
     */
    suspend fun listDevices(
        keys: DevelopmentKeys,
        projectId: Int,
    ) = withContext(ioDispatcher) {
        service.listDevices(
            apiKey = keys.apiKey,
            projectId = projectId,
        )
    }

    /**
     * Set the current name for a device.
     *
     * @param name New name for this device.
     */
    suspend fun renameDevice(
        keys: DevelopmentKeys,
        projectId: Int,
        deviceId: String,
        name: String
    ) = withContext(ioDispatcher) {
        service.renameDevice(
            apiKey = keys.apiKey,
            projectId = projectId,
            /*Encode thr url manually as retrofit does not correctly encode https://github.com/square/retrofit/issues/3080*/
            deviceId = deviceId.replace(":", "%3A"),
            renameDeviceRequest = RenameDeviceRequest(name = name),
        )
    }

    /**
     * Delete a device.
     *
     * When this device sends a new message to ingestion or connects to remote
     * management the device will be recreated.
     */
    suspend fun deleteDevice(
        keys: DevelopmentKeys,
        projectId: Int,
        deviceId: String
    ) = withContext(ioDispatcher) {
        service.deleteDevice(
            apiKey = keys.apiKey,
            projectId = projectId,
            /*Encode thr url manually as retrofit does not correctly encode https://github.com/square/retrofit/issues/3080*/
            deviceId = deviceId.replace(":", "%3A"),
        )
    }
}
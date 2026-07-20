/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.model

data class Sample(
    val id: Long,
    val filename: String,
    val signatureValidate: Boolean,
    val signatureMethod: String,
    val signatureKey: String,
    val created: String,
    val category: String,
    val coldstorageFilename: String,
    val label: String,
    val intervalMs: Number,
    val frequency: Number,
    val deviceName: String,
    val deviceType: String,
    val sensors: List<Sensor>,
    val valuesCount: Int,
    val totalLengthMs: Number,
    val added: String,
    val boundingBoxes: List<BoundingBox>
) {
    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Sample
        return id == other.id
    }

    data class Sensor(
        val name: String,
        val units: String
    )

    data class BoundingBox(
        val label: String,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
    )
}

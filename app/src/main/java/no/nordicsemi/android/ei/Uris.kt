/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei

import android.net.Uri
import androidx.core.net.toUri

object Uris {
    val ForgetPassword: Uri = "https://studio.edgeimpulse.com/forgot-password".toUri()
    val SignUp: Uri = "https://studio.edgeimpulse.com/signup".toUri()
}
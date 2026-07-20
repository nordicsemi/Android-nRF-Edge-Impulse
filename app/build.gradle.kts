/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */
plugins {
    // https://github.com/nordicsemi/Nordic-Gradle-Plugins/blob/main/plugins/src/main/kotlin/AndroidApplicationConventionPlugin.kt
    alias(libs.plugins.nordic.android.application)
    // https://github.com/nordicsemi/Nordic-Gradle-Plugins/blob/main/plugins/src/main/kotlin/HiltComposeConventionPlugin.kt
    alias(libs.plugins.nordic.feature.hilt.compose)
}

android {
    namespace = "no.nordicsemi.android.ei"
    defaultConfig {
        applicationId = "no.nordicsemi.android.nrfei"
    }
}

dependencies {
    implementation(nordic.ui)
    implementation(nordic.theme)
    implementation(nordic.ble.ktx)
    implementation(nordic.mcumgr.ble)
    implementation(libs.slf4j.timber)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.hilt.lifecycle.viewmodel)
    implementation(libs.androidx.activity.compose)
    implementation(libs.paging.compose)
    implementation(libs.paging.runtime)
    implementation(libs.paging.common)

    implementation(libs.kotlinx.datetime)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.accompanist.permissions)

    implementation(libs.coil.kt.compose)
    implementation(libs.gson)

    testImplementation(libs.truth)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.espresso.core)
}

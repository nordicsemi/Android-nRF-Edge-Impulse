/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    versionCatalogs {
        // Use Nordic Gradle Version Catalog with common external libraries versions.
        create("libs") {
            from("no.nordicsemi.gradle:version-catalog:3.1.2-3")
        }
        // Fixed versions for Nordic libraries.
        create("nordic") {
            from("no.nordicsemi.gradle:nordic-version-catalog:2026.07.03")
        }
    }
}
rootProject.name = "nRF Edge Impulse"
include (":app")

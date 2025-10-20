@file:OptIn(ExperimentalComposeLibrary::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization") version "2.2.0"
    id("org.kodein.mock.mockmp") version "2.0.2"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()


    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(libs.kotlinx.datetime)

            //Projects
            implementation(project(":features:condominio:data"))
            implementation(project(":features:pessoa:pessoa:data"))
            implementation(project(":features:condominio:data"))

            // Serializtionss
            api(libs.kotlinx.serialization.json)
            api(kotlin("reflect"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertk)

            implementation(libs.kotlinx.coroutines.test)

            implementation(project(":features:condominio:data"))

        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.zalamena.condominios.database"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mockmp {
    onTest {
        withHelper()
    }
}

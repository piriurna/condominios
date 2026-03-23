@file:OptIn(ExperimentalComposeLibrary::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
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
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.room.runtime.android)
        }

        androidUnitTest.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.androidx.core)
            implementation(libs.androidx.runner)
            implementation(libs.androidx.rules)
            implementation(libs.androidx.testExt.junit)

            // Koin testing
            implementation(libs.koin.test)
            implementation(libs.koin.junit4)
        }
        commonMain.dependencies {
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.datetime)

            implementation(libs.kotlinx.coroutines.test)

            // Ktor (needed for HttpClient type in DI wiring)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            api(project(":features:condominio:domain"))

            api(project(":features:condominio:data"))
            api(project(":features:condominio:domain"))
            api(project(":features:condominio:ui"))

            api(project(":features:pessoa:data"))
            api(project(":features:pessoa:domain"))
            api(project(":features:database"))
            api(project(":features:mock-data"))

            api(project(":features:login:data"))
            api(project(":features:login:domain"))
            api(project(":features:login:ui"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertk)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }

}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.zalamena.condominios.di"
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

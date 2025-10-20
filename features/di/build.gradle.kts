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

            api(project(":features:condominio:domain"))

            api(project(":features:condominio:data"))
            api(project(":features:condominio:domain"))
            api(project(":features:condominio:ui"))

            api(project(":features:pessoa:pessoa:data"))
            api(project(":features:pessoa:pessoa:domain"))
            api(project(":features:condominio:moradores:moradores:ui"))
            api(project(":features:condominio:apartamentos:add-apartamento:ui"))
            api(project(":features:pessoa:add-pessoa:ui"))
            api(project(":features:pessoa:add-pessoa:domain"))
            api(project(":features:condominio:moradores:add-morador:ui"))
            api(project(":features:database"))
            api(project(":features:mock-data"))
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

@file:OptIn(ExperimentalComposeLibrary::class)

import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
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
        }

        androidUnitTest.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.androidx.room.testing)
            implementation(libs.androidx.core)
            implementation(libs.androidx.runner)
            implementation(libs.androidx.rules)
            implementation(libs.androidx.testExt.junit)

            // Koin testing
            implementation(libs.koin.test)
            implementation(libs.koin.junit4)
        }
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(libs.kotlinx.datetime)

            implementation(project(":features:login:domain"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertk)
            implementation(project(":features:login:domain"))

            implementation(libs.kotlinx.coroutines.test)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }

    dependencies {
        add("kspAndroid", libs.room.compiler)
        add("kspIosSimulatorArm64", libs.room.compiler)
        add("kspIosArm64", libs.room.compiler)
        add("kspJvm", libs.room.compiler)
    }
}

android {
    namespace = "com.zalamena.condominios.login.data"
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

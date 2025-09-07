@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
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
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.koin.core)

            implementation(libs.kotlinx.datetime)


            //Projects
            implementation(project(":features:moradores:domain"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertk)
            implementation(compose.uiTest)

            implementation(project(":features:moradores:domain"))

        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }

    dependencies {
        // KSP support for Room Compiler.
        add("kspAndroid", libs.room.compiler)

    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

android {
    namespace = "com.zalamena.condominios.morades.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

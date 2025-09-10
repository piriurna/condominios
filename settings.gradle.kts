rootProject.name = "condominios"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
include(":features:moradores:data")
include(":features:moradores:domain")
include(":features:login:data")
include(":features:login:domain")
include(":features:database")
include(":features:moradores:ui")
include(":features:apartamentos:data")
include(":features:apartamentos:domain")
include(":features:apartamentos:ui")
include(":features:individuo:data")
include(":features:individuo:domain")
include(":features:individuo:ui")

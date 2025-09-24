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
include(":features:condominio:moradores:moradores:data")
include(":features:condominio:moradores:moradores:domain")
include(":features:condominio:moradores:moradores:ui")

include(":features:condominio:apartamentos:add-apartamento:ui")
include(":features:condominio:apartamentos:add-apartamento:domain")

include(":features:condominio:apartamentos:apartamento:data")
include(":features:condominio:apartamentos:apartamento:domain")
include(":features:condominio:apartamentos:apartamento:ui")

include(":features:login:data")
include(":features:login:domain")
include(":features:database")
include(":features:pessoa:pessoa:data")
include(":features:pessoa:pessoa:domain")
include(":features:pessoa:pessoa:ui")
include(":features:di")
include(":features:mock-data")
include(":features:common:ui")
include(":features:navigation:ui")
include(":features:pessoa:add-pessoa:domain")
include(":features:pessoa:add-pessoa:ui")
include(":features:condominio:moradores:add-morador:domain")
include(":features:condominio:moradores:add-morador:ui")

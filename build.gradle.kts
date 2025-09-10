plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    // Add KSP plugin
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}

// Root build.gradle.kts
tasks.register("runCommonTests") {
    group = "verification"
    description = "Runs common tests for JVM target"

    // Then run the JVM tests
    dependsOn(subprojects.flatMap { subproject ->
        if(subproject.name == "database" || subproject.name == "ui" || subproject.name == "di") return@flatMap emptyList()

        subproject.tasks.matching { task ->
            println("TASK NAME: ${subproject.name}")
            task.name.endsWith("Test")
                    && task.name.contains("jvm", ignoreCase = true)
        }
    })
}
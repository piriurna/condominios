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
    description = "Runs jvmTest for all feature modules (platform-agnostic)"

    dependsOn(subprojects.flatMap { subproject ->
        if (subproject.name == "database"
            || subproject.name == "di"
            || subproject.name == "mock-data"
            || subproject.path.contains(":common:")
            || subproject.path.contains(":navigation:")
        ) return@flatMap emptyList()

        subproject.tasks.matching { task ->
            task.name.endsWith("Test")
                && task.name.contains("jvm", ignoreCase = true)
        }
    })
}
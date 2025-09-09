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
}

// Root build.gradle.kts
tasks.register("runCommonTests") {
    group = "verification"
    description = "Runs common tests for JVM target"

    // Then run the JVM tests
    dependsOn(subprojects.flatMap { subproject ->
        if(subproject.name == "database") return@flatMap emptyList()

        subproject.tasks.matching { task ->
            println("TASK NAME: ${subproject.name}")
            task.name.endsWith("Test") && task.name.contains("jvmTest", ignoreCase = true)
        }
    })

    // Configure task ordering to ensure KSP runs before tests
    subprojects.forEach { subproject ->
        subproject.tasks.findByName("kspKotlinJvm")?.let { kspTask ->
            subproject.tasks.matching { it.name.contains("jvmTest", ignoreCase = true) }
                .forEach { testTask -> testTask.mustRunAfter(kspTask) }
        }
    }
}

// Add a task to help with KSP issues
tasks.register("cleanKsp") {
    group = "build"
    description = "Cleans KSP generated files"

    doFirst {
        subprojects.forEach { subproject ->
            val kspDir = File(subproject.projectDir, "build/generated/ksp")
            if (kspDir.exists()) {
                delete(kspDir)
                println("Deleted KSP directory for ${subproject.name}")
            }
        }
    }
}
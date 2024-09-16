// settings.gradle.kts

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()  // Aunque JCenter está en desuso, puede ser necesario para algunas dependencias antiguas
        maven("https://jitpack.io")  // Añadido para resolver dependencias desde JitPack
    }
}

rootProject.name = "AgroemNew"
include(":app")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Millia Connect"
include(":app")
include(":feature:portal")
include(":feature:notice")
include(":feature:result")
include(":feature:attendance")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:common")
include(":core:navigation")
include(":core:network")
include(":core:notification")
include(":core:auth")
include(":feature:rent")

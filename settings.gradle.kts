pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Note: jitpack.io removed — Jitsi is launched via browser Intent (no SDK needed)
        // All remaining deps (OSMDroid, Firebase, Room, etc.) are on Google/MavenCentral
    }
}

rootProject.name = "NabhaTeleMedicine"
include(":app")

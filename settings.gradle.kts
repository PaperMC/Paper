pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://wav.jfrog.io/artifactory/repo/")
    }
}

rootProject.name = "Paper"

include("Paper-API", "Paper-Server", "Paper-MojangAPI")

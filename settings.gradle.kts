pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://wav.jfrog.io/artifactory/repo/")
        mavenLocal()
    }
}

rootProject.name = "Paper"

include("Paper-API", "Paper-Server", "Paper-MojangAPI")

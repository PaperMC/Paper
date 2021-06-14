pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "Paper"

include("Paper-API", "Paper-Server", "Paper-MojangAPI")
